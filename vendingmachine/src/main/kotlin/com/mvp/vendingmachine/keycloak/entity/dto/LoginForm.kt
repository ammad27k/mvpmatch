package com.mvp.vendingmachine.keycloak.entity.dto

import feign.form.FormProperty

data class LoginForm(
    @FormProperty("grant_type")
    val grantType: String? = null,

    @FormProperty("scope")
    val scope: String? = null,

    @FormProperty("username")
    val username: String? = null,

    @FormProperty("password")
    val password: String? = null,

    @FormProperty("client_id")
    val clientId: String? = null,
)