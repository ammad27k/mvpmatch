package com.mvp.vendingmachine.common.utils

import java.math.BigDecimal

class DepositUtils {
    companion object {
        val centCoins: IntArray = intArrayOf(5, 10, 20, 50, 100)

        fun contains(coin : BigDecimal) : Boolean {
            centCoins.iterator().forEach {
                if (coin.toInt() == it) return true
            }
            return false
        }


        fun coinChange(amountRemaining: Int): ArrayList<Int> {
           val coinChange = CoinChange()
            coinChange.findMinimumCombination(amountRemaining, centCoins, centCoins.size)
            return coinChange.returnedCoins
        }

    }
}