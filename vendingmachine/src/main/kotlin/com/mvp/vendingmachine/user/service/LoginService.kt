package com.mvp.vendingmachine.user.service

import com.mvp.vendingmachine.common.FORWARD_IP
import com.mvp.vendingmachine.keycloak.KeyCloakFeignClient
import com.mvp.vendingmachine.keycloak.config.KeyCloakProperties
import com.mvp.vendingmachine.keycloak.entity.dto.OAuthToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate


@Service
class LoginService(private val keyCloakProperties: KeyCloakProperties,
                   private val keyCloakFeignClient: KeyCloakFeignClient,
                   private val restTemplate: RestTemplate
) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    // TODO use this one, this is not working becuase of JDK incompatibility issue, but not sure
    /*fun getToken(username: String?, password: String?, clientId: String?, ip: String?): OAuthToken? {
        try {
            var clientId = clientId
            if (!validClientId(clientId)) {
                clientId = keyCloakProperties.clientId
            }
            val loginForm = LoginForm(
                grantType = "password",
                scope = "openid",
                username = username,
                password = password,
                clientId = clientId,
            )
            var mapForm = mutableMapOf<String?, String?>()
            mapForm["grant_type"] = "password"
            mapForm["scope"] = "openid"
            mapForm["username"] =  username
            mapForm["password"] =  password
            mapForm["client_id"] = clientId
            return keyCloakFeignClient.getToken(ip , mapForm)

        } catch (ex: FeignException) {
            log.error("ClientErrorException", ex)
            throw ex
        } catch (exc: Exception) {
            log.error("ClientErrorException", exc)
            throw exc
        }
    }*/

    // TODO remove rest template implementation, this is a work arrount because feign client form data not working
    fun getToken(username: String?, password: String?, clientId: String?, ip: String?): OAuthToken? {
        try {
            var clientId = clientId
            val body: MultiValueMap<String, String> = LinkedMultiValueMap()
            if (!validClientId(clientId)) {
                clientId = keyCloakProperties.clientId
            }
            body.add("grant_type", "password")
            body.add("scope", "openid")
            body.add("username", username)
            body.add("password", password)
            body.add("client_id", clientId)
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
            headers.add(FORWARD_IP, ip)
            headers.setBasicAuth(keyCloakProperties.clientId!!, keyCloakProperties.clientSecret!!)
            val response: ResponseEntity<OAuthToken> = restTemplate.postForEntity(
                keyCloakProperties.tokenEndpoint!!, HttpEntity(body, headers),
                OAuthToken::class.java
            )
            return response.body

        } catch (exc: Exception) {
            log.error("ClientErrorException", exc)
            throw exc
        }
    }

    private fun validClientId(clientId: String?): Boolean {
        return clientId?.isNotEmpty() == true && clientId.length < 100 //trying to prevent large string attacks to keycloak.
    }

}