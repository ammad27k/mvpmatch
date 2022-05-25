package com.mvp.vendingmachine.vending.entity.dto

import java.math.BigDecimal

class PurchaseResponseDto(val changeCoins : MutableList<Int>,
                          val totalSpent : BigDecimal,
                          val product: Product
)