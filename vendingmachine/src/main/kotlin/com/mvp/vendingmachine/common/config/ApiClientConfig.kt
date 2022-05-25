package com.mvp.vendingmachine.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate




@Configuration
class ApiClientConfig {

    @Bean
    fun setupRest(): RestTemplate? {
        return RestTemplate()
    }
}