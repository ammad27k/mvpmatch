package com.mvp.vendingmachine

import com.mvp.vendingmachine.common.MVPConfigs
import com.mvp.vendingmachine.keycloak.KeyCloakFeignClient
import com.mvp.vendingmachine.keycloak.config.KeyCloakFeignClientConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

/*@SpringBootApplication(exclude =
    [SecurityAutoConfiguration::class,
    UserDetailsServiceAutoConfiguration::class,
        ManagementWebSecurityAutoConfiguration::class

])*/
@SpringBootApplication

@EnableConfigurationProperties(
    LiquibaseProperties::class
)
@EnableFeignClients(
    clients = [
        KeyCloakFeignClient::class
    ]
)
@MVPConfigs
class VendingMachineApplication

fun main(args: Array<String>) {
    runApplication<VendingMachineApplication>(*args)
}