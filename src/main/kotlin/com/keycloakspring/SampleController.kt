package com.keycloakspring

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class SampleController {
    @GetMapping("/anonymous")
    fun getAnonymousInfo(): String = "anonymous"

    @GetMapping("/user")
    fun getUserInfo(): String = "user info"

    @GetMapping("/admin")
    fun getAdminInfo(): String = "admin info"
}
