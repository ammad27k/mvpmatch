package com.mvp.vendingmachine.vending.entity.dto

import java.math.BigDecimal

data class DepositRequestDto(var depositAmount : BigDecimal? = BigDecimal.ZERO)
