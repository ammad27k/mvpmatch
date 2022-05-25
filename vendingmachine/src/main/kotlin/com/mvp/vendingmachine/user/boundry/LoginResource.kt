package com.mvp.vendingmachine.user.boundry

import com.mvp.vendingmachine.keycloak.entity.dto.OAuthToken
import com.mvp.vendingmachine.user.entity.dto.LoginRequest
import com.mvp.vendingmachine.user.service.LoginService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/login")
class LoginResource(private val loginService: LoginService) {

    @PostMapping(value = ["/"])
    fun getToken(@RequestBody req: LoginRequest): OAuthToken? {
       // TODO change Convert OAuthToken class to LoginResponse
        return loginService.getToken(
            req.username,
            req.password,
            req.clientId,
            req.ip
        )
    }

}