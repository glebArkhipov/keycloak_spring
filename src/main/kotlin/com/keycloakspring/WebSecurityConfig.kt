package com.keycloakspring

import net.minidev.json.JSONArray
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .oauth2Login()
            .userInfoEndpoint()
            .oidcUserService(oidcUserService())
            .and()
            .and()
            .authorizeRequests()
            .antMatchers("/anonymous/**").permitAll()
            .anyRequest().authenticated()
    }

    @Bean
    fun oidcUserService(): OAuth2UserService<OidcUserRequest, OidcUser> {
        val oidcServiceDelegate = OidcUserService()
        return OAuth2UserService { userRequest ->
            val oidcUser = oidcServiceDelegate.loadUser(userRequest)
            val authorities = oidcUser.authorities
                .filterIsInstance<OidcUserAuthority>()
                .map { it.authority }
                .map { SimpleGrantedAuthority(it) }
            DefaultOidcUser(authorities, oidcUser.idToken, oidcUser.userInfo)
        }
    }
}
