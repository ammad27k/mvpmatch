package com.mvp.vendingmachine.user.service

import com.mvp.vendingmachine.common.ErrorCodes
import com.mvp.vendingmachine.common.exception.ApplicationException
import com.mvp.vendingmachine.common.utils.DepositUtils
import com.mvp.vendingmachine.common.utils.UserUtils
import com.mvp.vendingmachine.keycloak.KeyCloakClient
import com.mvp.vendingmachine.user.entity.dto.SignupRequest
import com.mvp.vendingmachine.user.entity.dto.UserEntity
import com.mvp.vendingmachine.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserSignupService(
    private val keyCloakClient: KeyCloakClient,
    private val userRepository: UserRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)
    fun signUp(signupRequest: SignupRequest) {
        var userName = UserUtils.checkEmail(signupRequest.userName) // TODO this check should be remove according to requirement
        UserUtils.validatePassword(signupRequest.password)

        if(!DepositUtils.contains(signupRequest.deposit)) throw ApplicationException(ErrorCodes.INVALID_COIN_TO_DEPOSIT, "Invalid amount to be deposited")

        val userId = UUID.randomUUID().toString()
        // create user in keycloak.
        val keyCloakUserId = keyCloakClient.signupUser(userId, userName,
            signupRequest.password,
            signupRequest.role)


        userRepository.save(
            UserEntity( // TODO use map struct for converting entity to dto and from dto to entity
                userName = userName.toString(),
                ssoUserId = keyCloakUserId.toString(),
                deposit = signupRequest.deposit,
                role = signupRequest.role
            )
        )
        log.debug("keycloak user id $keyCloakUserId")
    }


}