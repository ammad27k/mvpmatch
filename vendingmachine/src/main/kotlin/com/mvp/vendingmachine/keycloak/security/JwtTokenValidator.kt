package com.mvp.vendingmachine.keycloak.security

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkException
import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mvp.vendingmachine.common.ErrorCodes
import com.mvp.vendingmachine.common.exception.InvalidTokenException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.*


class JwtTokenValidator(private val jwkProvider : JwkProvider) {
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun validateAuthorizationHeader(authorizationHeader: String?): AccessToken? {
        val tokenValue: String? = subStringBearer(authorizationHeader)
        validateToken(tokenValue)
        return AccessToken(tokenValue)
    }

    private fun validateToken(value: String?) {
        val decodedJWT = decodeToken(value)
        verifyTokenHeader(decodedJWT)
        verifySignature(decodedJWT)
        verifyPayload(decodedJWT)
    }

    private fun decodeToken(value: String?): DecodedJWT {
        value?:throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN,"Token has not been provided")
        val decodedJWT = JWT.decode(value)
        log.debug("Token decoded successfully")
        return decodedJWT
    }

    private fun verifyTokenHeader(decodedJWT: DecodedJWT) {
        if(decodedJWT.type != "JWT") InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN,"Token is not JWT type")
        log.debug("Token's header is correct")
    }

    private fun verifySignature(decodedJWT: DecodedJWT) {
        try {
            val jwk: Jwk = jwkProvider.get(decodedJWT.keyId)
            val algorithm: Algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
            algorithm.verify(decodedJWT)
            log.debug("Token's signature is correct")
        } catch (ex: JwkException) {
            throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN, "Token has invalid signature", ex)
        } catch (ex: SignatureVerificationException) {
            throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN, "Token has invalid signature", ex)
        }
    }

    private fun verifyPayload(decodedJWT: DecodedJWT) {
        val payloadAsJson: JsonObject = decodeTokenPayloadToJsonObject(decodedJWT)
        if (hasTokenExpired(payloadAsJson)) {
            throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN, "Token has expired")
        }
        log.debug("Token has not expired")
        if (!hasTokenRealmRolesClaim(payloadAsJson)) {
            throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN, "Token doesn't contain claims with realm roles")
        }
        log.debug("Token's payload contain claims with realm roles")
        if (!hasTokenScopeInfo(payloadAsJson)) {
            throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN, "Token doesn't contain scope information")
        }
        log.debug("Token's payload contain scope information")
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

    private fun hasTokenExpired(payloadAsJson: JsonObject): Boolean {
        val expirationDatetime = extractExpirationDate(payloadAsJson)
        return Instant.now().isAfter(expirationDatetime)
    }

    private fun extractExpirationDate(payloadAsJson: JsonObject): Instant {
        return try {
            Instant.ofEpochSecond(payloadAsJson.get("exp").getAsLong())
        } catch (ex: NullPointerException) {
            throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN,"There is no 'exp' claim in the token payload")
        }
    }

    private fun hasTokenRealmRolesClaim(payloadAsJson: JsonObject): Boolean {
        return try {
            payloadAsJson.getAsJsonObject("realm_access").getAsJsonArray("roles").size() > 0
        } catch (ex: NullPointerException) {
            false
        }
    }

    private fun hasTokenScopeInfo(payloadAsJson: JsonObject): Boolean {
        return payloadAsJson.has("scope")
    }

    private fun subStringBearer(authorizationHeader: String?): String? {
        return try {
            authorizationHeader?.substring(AccessToken.BEARER.length)
        } catch (ex: Exception) {
            throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN, "There is no AccessToken in a request header")
        }
    }


}