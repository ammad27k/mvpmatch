package com.mvp.vendingmachine.user.entity.dto

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(
    name = "users"
)
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0,

    @Column(name = "user_name", nullable = false)
    var userName: String? = null,

    @Column(name = "sso_user_id", nullable = false)
    var ssoUserId : String? = null,

    @Column(name = "deposit", nullable = true)
    var deposit : BigDecimal? = BigDecimal.ZERO,

    @Column(name = "role", nullable = false)
    var role : String? = null
)