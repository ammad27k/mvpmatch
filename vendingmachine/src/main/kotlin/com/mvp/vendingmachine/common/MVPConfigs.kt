package com.mvp.vendingmachine.common

import com.mvp.vendingmachine.common.exception.ErrorHandlingConfig
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(
    ErrorHandlingConfig::class
)
annotation class MVPConfigs
