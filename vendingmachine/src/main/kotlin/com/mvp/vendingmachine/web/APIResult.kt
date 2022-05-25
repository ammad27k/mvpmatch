package com.mvp.vendingmachine.web

import com.mvp.vendingmachine.common.EMPTY_STRING
import com.mvp.vendingmachine.common.exception.ApplicationException
import com.mvp.vendingmachine.common.exception.Error
import com.mvp.vendingmachine.common.exception.ErrorCode


sealed class Event<out T> {
    data class Success<out T>(val value: T? = null) : Event<T>()
    data class Error(val code: ErrorCode, val message: String) : Event<Nothing>()
}

class APIResult<T>(
    val success: Boolean,
    val message: String? = EMPTY_STRING,
    val data: T? = null,
    val errors: List<Error> = ArrayList()
) {
    companion object {
        fun <T> success(message: String, data: T?) = APIResult<T>(success = true, message = message, data = data)
        fun <T> fail(exception: ApplicationException) = APIResult<T>(success = false, message = exception.errorMsg,
            errors = arrayListOf(Error(exception.errorCode.code(), exception.errorMsg)), data = null)
    }
    fun <T> plus(errors: List<Error>): APIResult<T> {
        this.errors.plus(errors)
        return this as APIResult<T>
    }
}
