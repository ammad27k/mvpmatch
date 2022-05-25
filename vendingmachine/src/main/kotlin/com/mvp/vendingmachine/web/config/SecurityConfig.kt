package com.mvp.vendingmachine.web.config

import com.auth0.jwk.JwkProvider
import com.mvp.vendingmachine.keycloak.security.JwtTokenValidator
import com.mvp.vendingmachine.keycloak.security.KeycloakAuthenticationProvider
import com.mvp.vendingmachine.keycloak.security.KeycloakJwkProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


@Order(1)
@EnableWebSecurity
class SecurityConfig(
    @Value("\${spring.security.ignored}")
    private val nonSecureUrl : String,
    @Value("\${keycloak.jwk-set-uri}")
    private val jwkProviderUrl : String

    ): WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ?.and()
            ?.csrf()?.disable()
            ?.cors()
            ?.and()
            ?.exceptionHandling()
            ?.accessDeniedHandler(accessDeniedHandler())
            ?.and()
            ?.addFilterBefore(
                AccessTokenFilter(
                    jwtTokenValidator(keycloakJwkProvider()),
                    authenticationManagerBean(),
                    authenticationFailureHandler()
                ),
                BasicAuthenticationFilter::class.java
            )
    }

    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.antMatchers(
            nonSecureUrl, "/api/login/",
            "/api/signups/*");
    }

    @ConditionalOnMissingBean
    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider? {
        return KeycloakAuthenticationProvider()
    }

    @Bean
    fun authenticationFailureHandler(): AuthenticationFailureHandler? {
        return AccessTokenAuthenticationFailureHandler()
    }
    @Bean
    fun jwtTokenValidator(jwkProvider: JwkProvider?): JwtTokenValidator? {
        return JwtTokenValidator(jwkProvider!!)
    }

    @Bean
    fun keycloakJwkProvider(): JwkProvider? {
        return KeycloakJwkProvider(jwkProviderUrl)
    }

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler? {
        return AuthorizationAccessDeniedHandler()
    }
}