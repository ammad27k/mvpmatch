package com.mvp.vendingmachine.common.exception

import com.mvp.vendingmachine.web.APIResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.ConversionFailedException
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.ServletWebRequest

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
class ExceptionTranslator {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Value("\${spring.application.name:missing-app-name}")
    private val applicationName: String = ""


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<APIResult<String>>? {
        val fieldErrors = ex.bindingResult.fieldErrors.filterIsInstance(FieldError::class.java)
            .map { Error(checkNotNull(it.code), it.field.plus(" ").plus(it.defaultMessage)) }.toList()
        fieldErrors.plus(
            ex.bindingResult.globalErrors.filterIsInstance(ObjectError::class.java)
                .map { it.code?.let { it1 -> Error(it1, "${it.defaultMessage}") } }.toList()
        )
        return ResponseEntity(
            APIResult(false, "Invalid Data fields.", null, fieldErrors),
            HttpStatus.BAD_REQUEST
        )
    }


    @ExceptionHandler(ConcurrencyFailureException::class)
    fun handleConcurrencyFailure(
        ex: ConcurrencyFailureException,
        request: ServletWebRequest
    ): ResponseEntity<APIResult<String>>? {
        log.error("Concurrency Error occurred.", ex)
        return ResponseEntity(
            APIResult(false, "Concurrency Error.", null, emptyList()),
            HttpStatus.CONFLICT
        )
    }


    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<APIResult<String>>? =
        ResponseEntity(
            APIResult(false, "Access Denied.", null, emptyList()),
            HttpStatus.FORBIDDEN
        )

    @ExceptionHandler(HttpClientErrorException::class)
    fun handleAccessDeniedException(ex: HttpClientErrorException): ResponseEntity<APIResult<String>>? =
        ResponseEntity(
            APIResult(false, "Un Authorized access", null, emptyList()),
            HttpStatus.UNAUTHORIZED
        )

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(
        ex: ApplicationException, request: ServletWebRequest
    ): ResponseEntity<APIResult<String>> {
        log.error("Error occurred for API: ${request.httpMethod} ${request.request.requestURI}. ErrorCode: ${ex.errorCode.code()}, ErrorMsg: ${ex.errorMsg}")
        return ResponseEntity(APIResult.fail(ex), HttpStatus.OK)
    }

    @ExceptionHandler(ConversionFailedException::class)
    fun handleConversionFailedException(ex: ConversionFailedException, request: ServletWebRequest): ResponseEntity<APIResult<Any>> {
        log.error("Error occurred for API: ${request.httpMethod} ${request.request.requestURI}", ex)
        return ResponseEntity(
            APIResult(false, "Conversion failed for the value", ex.value, emptyList()),
            HttpStatus.BAD_REQUEST
        )
    }

    /**
     * For the remaining exceptions, do not expose details in the API response.
     */
    @ExceptionHandler(RuntimeException::class)
    fun handleOtherExceptions(ex: RuntimeException, request: ServletWebRequest): ResponseEntity<APIResult<String>> {
        log.error("Error occurred for API: ${request.httpMethod} ${request.request.requestURI}", ex)
        val apiResult = APIResult(
            false,
            "Processing error occurred.",
            "",
            listOf(Error(errorCode = "INTERNAL_ERROR", errorMsg = ex.message))
        )
        return ResponseEntity(apiResult, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
