package com.mvp.vendingmachine.user.entity.dto

import java.math.BigDecimal

data class SignupRequest(
    var userName : String,
    var password : String,
    var deposit: BigDecimal,
    var role : String
)
