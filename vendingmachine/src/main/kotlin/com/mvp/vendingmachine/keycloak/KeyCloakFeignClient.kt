package com.mvp.vendingmachine.keycloak

import com.mvp.vendingmachine.common.FORWARD_IP
import com.mvp.vendingmachine.keycloak.config.KeyCloakFeignClientConfig
import com.mvp.vendingmachine.keycloak.entity.dto.LoginForm
import com.mvp.vendingmachine.keycloak.entity.dto.OAuthToken
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(
    name = "keycloak",
    url = "\${keycloak.tokenEndpoint}",
    configuration = [KeyCloakFeignClientConfig::class]
)
interface KeyCloakFeignClient {

    @RequestMapping(
        method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE]
    )
    fun getToken(
        @RequestHeader(FORWARD_IP) forwardId : String?,
        @RequestBody formParams : MutableMap<String?, String?>,
    ): OAuthToken
}