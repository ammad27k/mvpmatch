package com.mvp.vendingmachine.vending.entity.dto

import java.math.BigDecimal

data class SellerRequestDto(
    val amountAvailable : BigDecimal?,
    val cost : BigDecimal?,
    val productName : String?
)
