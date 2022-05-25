package com.mvp.vendingmachine.vending.repository

import com.mvp.vendingmachine.user.entity.dto.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
    fun findBySellerId(sellerId : Long) : List<ProductEntity>?
}