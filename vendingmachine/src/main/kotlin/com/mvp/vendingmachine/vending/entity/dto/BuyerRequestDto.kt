package com.mvp.vendingmachine.vending.entity.dto

import java.math.BigDecimal

data class BuyerRequestDto(val productId : String, val amount : BigDecimal)
