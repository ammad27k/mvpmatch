package com.mvp.vendingmachine.common.utils


class CoinChange {
    val MAX = 100000
    var dp = IntArray(MAX + 1)
    var returnedCoins = arrayListOf<Int>()

    private fun countMinCoins(
        n: Int,
        C: IntArray, m: Int
    ): Int {
        // Base case
        if (n == 0) {
            dp[0] = 0
            return 0
        }

        // If previously computed
        // subproblem occurred
        if (dp[n] != -1) return dp.get(n)

        // Initialize result
        var ret = Int.MAX_VALUE

        // Try every coin that has smaller
        // value than n
        for (i in 0 until m) {
            if (C[i] <= n) {
                val x = countMinCoins(
                    n - C[i],
                    C, m
                )

                // Check for Integer.MAX_VALUE to avoid
                // overflow and see if result
                // can be minimized
                if (x != Int.MAX_VALUE) ret = Math.min(ret, 1 + x)
            }
        }

        dp[n] = ret
        return ret
    }

    private fun findSolution(
        n: Int,
        C: IntArray, m: Int
    ) {
        // Base Case
        if (n == 0) {
            return
        }
        for (i in 0 until m) {
            // Try every coin that has
            // value smaller than n
            if (n - C[i] >= 0 &&
                dp[n - C[i]] + 1 == dp[n]
            ) {
                // Add current denominations
                returnedCoins.add(C[i])
                // Backtrack
                findSolution(n - C[i], C, m)
                break
            }
        }
    }

    // Function to find the minimum
    // combinations of coins for value X
    fun findMinimumCombination(
        X: Int,
        C: IntArray, N: Int
    ) {
        // Initialize dp with -1
        for (i in 0 until dp.size) dp[i] = -1

        // Min coins
        val isPossible = countMinCoins(X, C, N)

        // If no solution exists
        if (isPossible != Int.MAX_VALUE) {
            findSolution(X, C, N)
        }
    }
}