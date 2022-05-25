package com.mvp.vendingmachine.web.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AllowedRoles(val roles: Array<String>)
