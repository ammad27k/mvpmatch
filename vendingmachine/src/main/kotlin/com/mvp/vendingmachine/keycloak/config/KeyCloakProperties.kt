package com.mvp.vendingmachine.keycloak.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("keycloak")
class KeyCloakProperties {
    var baseUri: String? = null
    var adminUri: String? = null
    var clientId: String? = null
    var realm: String? = null
    var clientSecret: String? = null
    var adminUsername: String? = null
    var adminPassword: String? = null
    var userinfoEndpoint: String? = null
    var introspectionEndpoint: String? = null
    var tokenEndpoint: String? = null
    var logoutEndpoint: String? = null
    var jwtPublicKey: String? = null
}