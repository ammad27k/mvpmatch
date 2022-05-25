package com.mvp.vendingmachine.keycloak.config

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KeyCloakClientConfig {

    @Bean
    fun setUp(properties: KeyCloakProperties) : Keycloak{
        return KeycloakBuilder.builder()
            .serverUrl(properties.adminUri + "/auth")
            .realm(properties.realm)
            .username(properties.adminUsername)
            .password(properties.adminPassword)
            .clientSecret(properties.clientSecret)
            .grantType(OAuth2Constants.PASSWORD)
            .clientId(properties.clientId)
            .resteasyClient(ResteasyClientBuilder().connectionPoolSize(10).build())
            .build()
    }
}