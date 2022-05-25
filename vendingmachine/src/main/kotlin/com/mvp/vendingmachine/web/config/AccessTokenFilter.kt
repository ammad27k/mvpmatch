package com.mvp.vendingmachine.web.config

import com.mvp.vendingmachine.common.ErrorCodes
import com.mvp.vendingmachine.common.exception.InvalidTokenException
import com.mvp.vendingmachine.keycloak.security.AccessToken
import com.mvp.vendingmachine.keycloak.security.JwtAuthentication
import com.mvp.vendingmachine.keycloak.security.JwtTokenValidator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.util.matcher.AnyRequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AccessTokenFilter : AbstractAuthenticationProcessingFilter {
    private var tokenVerifier: JwtTokenValidator? = null
    val log: Logger = LoggerFactory.getLogger(this::class.java)

    constructor(jwtTokenValidator: JwtTokenValidator?,
                authenticationManager: AuthenticationManager?,
                authenticationFailureHandler: AuthenticationFailureHandler?) : super(AnyRequestMatcher.INSTANCE) {

        setAuthenticationManager(authenticationManager)
        setAuthenticationFailureHandler(authenticationFailureHandler)
        this.tokenVerifier = jwtTokenValidator
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val authorizationHeader: String? = extractAuthorizationHeaderAsString(request)
        val accessToken: AccessToken? = tokenVerifier!!.validateAuthorizationHeader(authorizationHeader)
        return authenticationManager
            .authenticate(JwtAuthentication(accessToken))
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        log.info("Successfully authentication for the request {}", request?.getRequestURI());

        SecurityContextHolder.getContext().authentication = authResult
        chain?.doFilter(request, response)
    }
    private fun extractAuthorizationHeaderAsString(request: HttpServletRequest?): String? {
        return try {
            request?.getHeader("Authorization")
        } catch (ex: Exception) {
            throw InvalidTokenException(ErrorCodes.INVALID_ACCESS_TOKEN,"There is no Authorization header in a request", ex)
        }
    }
}