package com.mvp.vendingmachine.keycloak.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

class KeycloakAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?) =  authentication
    override fun supports(authentication: Class<*>?): Boolean {
        return JwtAuthentication::class.java.isAssignableFrom(authentication)
    }
}