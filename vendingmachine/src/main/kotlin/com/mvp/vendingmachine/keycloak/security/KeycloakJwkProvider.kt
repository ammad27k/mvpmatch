package com.mvp.vendingmachine.keycloak.security

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.NetworkException
import com.auth0.jwk.SigningKeyNotFoundException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


class KeycloakJwkProvider : JwkProvider {
    private var uri: URI? = null
    private var reader: ObjectReader? = null

    constructor(jwkProviderUrl : String) {
        try {
            this.uri = URI(jwkProviderUrl).normalize()
        } catch (e: URISyntaxException) {
            throw IllegalArgumentException("Invalid jwks uri", e)
        }
        this.reader = ObjectMapper().readerFor(MutableMap::class.java)
    }

    override fun get(keyId: String?): Jwk {
        val jwks: List<Jwk>? = getAll()
        jwks?: throw SigningKeyNotFoundException("No key found in " + uri.toString() + " with kid " + keyId, null)

        if (keyId == null && jwks?.size == 1) {
            return jwks[0]
        }
        if (keyId != null) {
            for (jwk in jwks) {
                if (keyId == jwk.id) {
                    return jwk
                }
            }
        }
        throw SigningKeyNotFoundException("No key found in " + uri.toString() + " with kid " + keyId, null)
    }

    @Throws(SigningKeyNotFoundException::class)
    private fun getAll(): List<Jwk>? {
        val jwks: MutableList<Jwk> = mutableListOf()
        val keys = getJwks()?.get("keys") as List<Map<String, Any>>?
        if (keys == null || keys.isEmpty()) {
            throw SigningKeyNotFoundException("No keys found in " + uri.toString(), null)
        }
        try {
            for (values in keys) {
                jwks.add(Jwk.fromValues(values))
            }
        } catch (e: IllegalArgumentException) {
            throw SigningKeyNotFoundException("Failed to parse jwk from json", e)
        }
        return jwks
    }

    @Throws(SigningKeyNotFoundException::class)
    private fun getJwks(): Map<String?, Any?>? {
        return try {
            val client = HttpClient.newHttpClient()
            val request = HttpRequest.newBuilder()
                .uri(uri)
                .headers("Accept", "application/json")
                .GET()
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            reader!!.readValue(response.body())
        } catch (e: IOException) {
            throw NetworkException("Cannot obtain jwks from url " + uri.toString(), e)
        } catch (e: InterruptedException) {
            throw NetworkException("Cannot obtain jwks from url " + uri.toString(), e)
        }
    }
}