package com.mvp.vendingmachine.web.config

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthorizationAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        response?.status = HttpStatus.FORBIDDEN.value()
        response?.contentType = "application/json;charset=UTF-8"
        response?.writer?.write(createErrorBody(accessDeniedException));
    }

    private fun createErrorBody(exception: AccessDeniedException?): String? {
        val exceptionMessage = JsonObject()
        exceptionMessage.addProperty("code", HttpStatus.FORBIDDEN.value())
        exceptionMessage.addProperty("reason", HttpStatus.FORBIDDEN.reasonPhrase)
        exceptionMessage.addProperty("timestamp", Instant.now().toString())
        exceptionMessage.addProperty("message", exception?.message)
        return Gson().toJson(exceptionMessage)
    }
}