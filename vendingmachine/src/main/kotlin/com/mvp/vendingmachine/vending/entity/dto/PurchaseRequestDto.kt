package com.mvp.vendingmachine.vending.entity.dto

import java.math.BigDecimal

data class PurchaseRequestDto(val productId : Long, val amount : BigDecimal? = BigDecimal.ZERO )