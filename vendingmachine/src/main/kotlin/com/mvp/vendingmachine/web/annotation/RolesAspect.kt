package com.mvp.vendingmachine.web.annotation

import com.mvp.vendingmachine.common.ErrorCodes
import com.mvp.vendingmachine.common.exception.ApplicationException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import kotlin.streams.toList

@Aspect
@Component
class RolesAspect {

    @Before("@annotation(expectedRoles)")
    fun before(joinPoint: JoinPoint, expectedRoles: AllowedRoles) {
        val authorities = SecurityContextHolder.getContext()?.authentication?.authorities
        val roles = authorities?.stream()?.map { mapper -> mapper.authority }
            ?.toList()

        if(roles?.containsAll(expectedRoles.roles.toList()) != true) {
            throw ApplicationException(ErrorCodes.INVALID_ROLE, "You don't have permission/role for this access")
        }
    }
}
