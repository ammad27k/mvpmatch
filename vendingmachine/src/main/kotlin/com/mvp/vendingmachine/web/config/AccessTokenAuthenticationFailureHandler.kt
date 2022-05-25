package com.mvp.vendingmachine.web.config

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AccessTokenAuthenticationFailureHandler : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.contentType = "application/json;charset=UTF-8"
        response?.addHeader("Cache-Control", "no-cache, no-store, must-revalidate")
        response?.addHeader("Pragma", "no-cache")
        response?.addHeader("Expires", "0")
        response?.writer?.write(createErrorBody(exception))
    }

    private fun createErrorBody(exception: AuthenticationException?): String? {
        val exceptionMessage = JsonObject()
        exceptionMessage.addProperty("code", HttpStatus.UNAUTHORIZED.value())
        exceptionMessage.addProperty("reason", HttpStatus.UNAUTHORIZED.reasonPhrase)
        exceptionMessage.addProperty("timestamp", Instant.now().toString())
        exceptionMessage.addProperty("message", exception?.message)
        return Gson().toJson(exceptionMessage)
    }
}