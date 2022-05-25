package com.mvp.vendingmachine.keycloak.entity.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OAuthToken(
    @JsonProperty("access_token")
    val accessToken : String? = null,
    @JsonProperty("refresh_token")
    val refreshToken : String? = null,
    @JsonProperty("expires_in")
    val expiresIn : Int? = null,
    @JsonProperty("refresh_expires_in")
    val refreshExpiresIn : Int? = null,
    @JsonProperty("token_type")
    val tokenType : String? = null,
    @JsonProperty("id_token")
    val idToken : String? = null,
    @JsonProperty("scope")
    val scope : String? = null,
    @JsonProperty("session_state")
    val sessionState : String? = null
)
