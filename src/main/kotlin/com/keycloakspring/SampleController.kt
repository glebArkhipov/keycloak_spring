package com.keycloakspring

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class SampleController {
    @GetMapping("/anonymous")
    fun getAnonymousInfo(): String = "anonymous"

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    fun getUserInfo(): String = "user info"

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAdminInfo(): String = "admin info"
}