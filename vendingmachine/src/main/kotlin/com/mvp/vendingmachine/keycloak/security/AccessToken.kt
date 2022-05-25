package com.mvp.vendingmachine.keycloak.security

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mvp.vendingmachine.common.ErrorCodes
import com.mvp.vendingmachine.common.exception.InvalidTokenException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import java.util.stream.StreamSupport


class AccessToken(val value : String?) {

    fun getAuthorities(): Collection<GrantedAuthority?>? {
        val payloadAsJson = getPayloadAsJsonObject()
        val hasClientBaseResourceAccess = payloadAsJson?.getAsJsonObject("resource_access")
            ?.has("microservice")

        var jsonElement : Stream<JsonElement>? = null
        if(hasClientBaseResourceAccess == true) {
            jsonElement = StreamSupport.stream(
                payloadAsJson?.getAsJsonObject("resource_access")
                    ?. getAsJsonObject("microservice")
                    ?.getAsJsonArray("roles")
                    ?.spliterator(), false
            )
        } else {
            jsonElement = StreamSupport.stream(
                payloadAsJson?.getAsJsonObject("realm_access")
                    ?.getAsJsonArray("roles")
                    ?.spliterator(), false
            )
        }

        return jsonElement
            ?.map { obj: JsonElement -> obj.asString }
            ?.map { role: String? ->
                SimpleGrantedAuthority(
                    role
                )
            }
            ?.collect(Collectors.toList())
    }

    fun getUsername(): String? {
        val payloadAsJson = getPayloadAsJsonObject()
        return payloadAsJson?.getAsJsonPrimitive("preferred_username")?.asString?: ""
    }

    private fun getPayloadAsJsonObject(): JsonObject? {
        val decodedJWT = decodeToken(value)
        return decodeTokenPayloadToJsonObject(decodedJWT)
    }

    private fun decodeToken(value: String?): DecodedJWT {
        value?:throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN,"Token has not been provided")
        return JWT.decode(value)
    }

    private fun decodeTokenPayloadToJsonObject(decodedJWT: DecodedJWT): JsonObject {
        return try {
            val payloadAsString = decodedJWT.payload
            Gson().fromJson(
                String(Base64.getDecoder().decode(payloadAsString), StandardCharsets.UTF_8),
                JsonObject::class.java
            )
        } catch (exception: RuntimeException) {
            throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN,"Invalid JWT or JSON format of each of the jwt parts", exception)
        }
    }

    companion object {
        const val BEARER = "Bearer "
    }
}