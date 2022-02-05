package com.keycloakspring

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder


const val SUB: String = "a72b7020-d5dd-4848-b219-80bb381a4110"

@TestConfiguration
class SecurityConfig {

    @Bean
    fun jwtDecoder(): JwtDecoder = CustomJwtDecoder()
}

private class CustomJwtDecoder : JwtDecoder {
    override fun decode(token: String): Jwt =
        Jwt.withTokenValue(token)
            .header("alg", "none")
            .claim("sub", SUB)
            .build()
}

