package com.keycloakspring

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {
    @GetMapping("/anonymous")
    fun getAnonymousInfo(): String = "anonymous"

    @GetMapping("/user")
    fun getUserInfo(): String {
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        return "user id: ${jwt.subject}"
    }

    @GetMapping("/writer")
    fun getAdminInfo(): String = "writer info"
}
