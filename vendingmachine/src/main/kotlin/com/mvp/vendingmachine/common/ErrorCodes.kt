package com.mvp.vendingmachine.common

import com.mvp.vendingmachine.common.exception.ErrorCode


enum class ErrorCodes : ErrorCode {
    USER_ALREADY_EXISTS,
    INVALID_EMAIL_ADDRESS,
    INVALID_PASSWORD_FORMAT,
    INVALID_ACCESS_TOKEN,
    INVALID_ROLE,
    USER_NOT_EXIST,
    NOT_PRODUCT_FOUND,
    DEPOSIT_AMOUNT_NOT_VALID,
    INVALID_COIN_TO_DEPOSIT;

    override fun code(): String = this.name
}
