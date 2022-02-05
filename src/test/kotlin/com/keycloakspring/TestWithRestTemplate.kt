package com.keycloakspring

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [SecurityConfig::class])
@ActiveProfiles("test")
class TestWithRestTemplate(
    @Autowired val restTemplate: TestRestTemplate
) {
    @Test
    fun test() {
        val resp = restTemplate.exchange(
            "/user",
            HttpMethod.GET,
            getEmptyRequestWithAuthHeader(),
            String::class.java
        )
        val body = checkNotNull(resp.body)

        Assertions.assertEquals(HttpStatus.OK, resp.statusCode)
        Assertions.assertEquals("user id: $SUB", body)
    }

    private fun getEmptyRequestWithAuthHeader() = HttpEntity<Void>(getAuthHeader())

    private fun getAuthHeader(): HttpHeaders {
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer token")
        return headers
    }
}
