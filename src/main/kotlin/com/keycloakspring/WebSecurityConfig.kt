package com.keycloakspring

import com.nimbusds.jose.shaded.json.JSONArray
import com.nimbusds.jose.shaded.json.JSONObject
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import java.util.stream.Collectors

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter())
            .and()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/anonymous/**").permitAll()
            .antMatchers("/writer/**").hasAnyRole("writer")
            .anyRequest().authenticated()
    }

    @Bean
    fun jwtAuthenticationConverter(): Converter<Jwt, AbstractAuthenticationToken> {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter())
        return jwtAuthenticationConverter
    }

    @Bean
    fun jwtGrantedAuthoritiesConverter(): Converter<Jwt, Collection<GrantedAuthority>> {
        val delegate = JwtGrantedAuthoritiesConverter()
        return JwtToAuthority(delegate)
    }
}

class JwtToAuthority(
    private val delegate: JwtGrantedAuthoritiesConverter
): Converter<Jwt, Collection<GrantedAuthority>> {
    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        val grantedAuthorities: MutableCollection<GrantedAuthority> = delegate.convert(jwt) as MutableCollection<GrantedAuthority>
        if (jwt.getClaim<Any?>("realm_access") == null) {
            return grantedAuthorities
        }
        val realmAccess: JSONObject = jwt.getClaim("realm_access")
        if (realmAccess["roles"] == null) {
            return grantedAuthorities
        }
        val roles: JSONArray = realmAccess["roles"] as JSONArray
        val keycloakAuthorities: List<GrantedAuthority> = roles.stream().map { role ->
            SimpleGrantedAuthority(
                "ROLE_$role"
            )
        }.collect(Collectors.toList())
        grantedAuthorities.addAll(keycloakAuthorities)
        return grantedAuthorities
    }
}
