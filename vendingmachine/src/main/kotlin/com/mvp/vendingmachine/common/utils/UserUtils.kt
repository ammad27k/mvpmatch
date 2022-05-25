package com.mvp.vendingmachine.common.utils

import com.mvp.vendingmachine.common.ErrorCodes
import com.mvp.vendingmachine.common.exception.ApplicationException
import java.util.*

class UserUtils {
    companion object {
        fun checkEmail(email: String?): String? {
            email?:throw ApplicationException(ErrorCodes.INVALID_EMAIL_ADDRESS, "Please provide an email address")
            return email.lowercase(Locale.getDefault()).trim { it <= ' ' }
        }

        fun validatePassword(plainPassword: String?) {
            var correct = plainPassword?.isEmpty() == false && plainPassword?.length >= 10

            when(correct) {
                true -> {
                    var upper = false
                    var lower = false
                    var digit = false
                    for (c in plainPassword?.toList()!!) {
                        if (Character.isUpperCase(c!!)) upper = true
                        if (Character.isLowerCase(c!!)) lower = true
                        if (Character.isDigit(c!!)) digit = true
                    }
                    correct = upper && lower && digit
                }
            }

            if (!correct) ApplicationException(ErrorCodes.INVALID_PASSWORD_FORMAT,
                "Your password must be 10 characters or more in length, contain 1 lowercase " +
                        "and 1 uppercase character and 1 number"
            )

        }
    }
}