package com.mvp.vendingmachine.user.boundry

import com.mvp.vendingmachine.user.entity.dto.SignupRequest
import com.mvp.vendingmachine.user.service.UserSignupService
import com.mvp.vendingmachine.web.APIResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/signups")
class UserSignUpResource(private val userSignupService: UserSignupService) {

    @PostMapping(value = ["/users"])
    fun signup(@RequestBody req: SignupRequest) : APIResult<Unit> {
        userSignupService.signUp(req)
        return APIResult(success = true, message = "Signup successfully completed", data = null)
    }
}