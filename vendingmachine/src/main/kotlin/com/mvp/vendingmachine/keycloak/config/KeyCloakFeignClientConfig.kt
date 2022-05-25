package com.mvp.vendingmachine.keycloak.config

import feign.auth.BasicAuthRequestInterceptor
import feign.codec.Encoder
import feign.form.FormEncoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean


open class KeyCloakFeignClientConfig {

    @Value("\${keycloak.clientId}")
    lateinit var clientId: String

    @Value("\${keycloak.clientSecret}")
    lateinit var clientSecret: String


    @Bean
    open fun basicAuthRequestInterceptor(): BasicAuthRequestInterceptor? {
        return BasicAuthRequestInterceptor(clientId, clientSecret)
    }

    @Bean
    open fun feignFormEncoder(): Encoder? {
        return FormEncoder()
    }
}