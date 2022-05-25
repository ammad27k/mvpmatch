package com.mvp.vendingmachine.user.entity.dto

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "products")
class ProductEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0,

    @Column(name = "product_name", nullable = false)
    var productName: String? = null,
    @Column(name = "seller_id", nullable = false)
    var sellerId : Long? = null,
    @Column(name = "amount_available", nullable = true)
    var availableAmount : BigDecimal? = BigDecimal.ZERO,
    @Column(name = "cost", nullable = false)
    var cost : BigDecimal? = BigDecimal.ZERO
)
