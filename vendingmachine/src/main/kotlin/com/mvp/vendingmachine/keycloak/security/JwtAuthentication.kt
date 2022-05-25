package com.mvp.vendingmachine.keycloak.security

import org.springframework.security.authentication.AbstractAuthenticationToken

class JwtAuthentication(private val accessToken: AccessToken?) :
    AbstractAuthenticationToken(accessToken?.getAuthorities()) {
    override fun getCredentials(): String? {
        return accessToken?.value
    }

    override fun getPrincipal(): String? {
        return accessToken?.getUsername()
    }
}