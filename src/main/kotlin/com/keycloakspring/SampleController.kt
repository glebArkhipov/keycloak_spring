package com.keycloakspring

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class SampleController {
    @GetMapping("/anonymous")
    fun getAnonymousInfo(): String = "anonymous"

    @GetMapping("/user")
    fun getUserInfo(): String = "user info"

    @GetMapping("/writer")
    fun getAdminInfo(): String = "writer info"
}
