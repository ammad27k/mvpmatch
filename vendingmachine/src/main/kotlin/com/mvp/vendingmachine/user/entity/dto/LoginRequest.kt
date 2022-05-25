package com.mvp.vendingmachine.user.entity.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginRequest(
    val username : String,
    val password : String,
    val clientId : String,
    val ip : String // TODO remove this from here and intercept the ip from the request
)