package com.mvp.vendingmachine.user.service

import com.mvp.vendingmachine.common.ErrorCodes
import com.mvp.vendingmachine.common.NO_FOR_MULTIPLE_OF
import com.mvp.vendingmachine.common.exception.ApplicationException
import com.mvp.vendingmachine.common.utils.DepositUtils
import com.mvp.vendingmachine.user.repository.UserRepository
import com.mvp.vendingmachine.vending.entity.dto.DepositRequestDto
import com.mvp.vendingmachine.vending.entity.dto.DepositResponseDto
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class UserService(private val userRepository: UserRepository) {

    fun updateDepositAmount(userName : String, depositRequestDto: DepositRequestDto ) : DepositResponseDto {
        val userEntity = userRepository.findByUserName(userName)
        userEntity?: throw ApplicationException(ErrorCodes.USER_NOT_EXIST, "user not exist")

        if(!DepositUtils.contains(depositRequestDto.depositAmount!!)) throw ApplicationException(ErrorCodes.INVALID_COIN_TO_DEPOSIT, "Invalid amount to be deposited")

        userEntity.deposit?.let {
            validateAmountMultipleOfNumber(it.plus(depositRequestDto.depositAmount!!), NO_FOR_MULTIPLE_OF)
        }?: validateAmountMultipleOfNumber(depositRequestDto.depositAmount!!, NO_FOR_MULTIPLE_OF)

        userEntity.deposit = userEntity.deposit?.plus(depositRequestDto.depositAmount!!)
        userRepository.save(userEntity)
        return DepositResponseDto(true)
    }

    fun validateAmountMultipleOfNumber(depositAmount : BigDecimal?, number : String) {
        val remainder = depositAmount?.remainder(BigDecimal(number))
        if (BigDecimal.ZERO.compareTo(remainder) != 0) throw ApplicationException(ErrorCodes.DEPOSIT_AMOUNT_NOT_VALID)
    }
}