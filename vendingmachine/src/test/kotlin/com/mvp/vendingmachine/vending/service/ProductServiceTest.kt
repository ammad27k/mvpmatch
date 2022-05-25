package com.mvp.vendingmachine.vending.service

import com.mvp.vendingmachine.common.exception.ApplicationException
import com.mvp.vendingmachine.user.entity.dto.ProductEntity
import com.mvp.vendingmachine.user.entity.dto.UserEntity
import com.mvp.vendingmachine.user.repository.UserRepository
import com.mvp.vendingmachine.vending.entity.dto.PurchaseRequestDto
import com.mvp.vendingmachine.vending.entity.dto.SellerRequestDto
import com.mvp.vendingmachine.vending.repository.ProductRepository
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ProductServiceTest {

    @Mock
    lateinit var  productRepository: ProductRepository
    @Mock
    lateinit var userRepository: UserRepository

    lateinit var underTest : ProductService

    @BeforeEach
    fun setUp() {
        underTest = ProductService(productRepository, userRepository);
    }

    @Test
    fun validateAmountMultipleOfNumberThrowError() {
        assertThrows<ApplicationException> { underTest.validateAmountMultipleOfNumber(
            BigDecimal("30.00"), "5")
        }
    }

    @Test
    fun validateAmountMultipleOfNumberSuccess() {
        underTest.validateAmountMultipleOfNumber(
            BigDecimal("30.00"), "5")
    }

    @Test
    fun addProduct() {
        Mockito.`when`(userRepository.findByUserName(any()))
            .thenReturn(getUserEntity("seller"))

        Mockito.`when`(productRepository.save(any()))
            .thenReturn(getProductEntity())

        val response = underTest.addProduct(
            getSellerRequestDto(BigDecimal("100.00"), BigDecimal("10.00"), "Mango"),
            "aarshad"
        )
        assertNotNull(response)
    }

    @Test
    fun getProducts() {
    }

    @Test
    fun purchaseProduct() {

        Mockito.`when`(userRepository.findByUserName(any()))
            .thenReturn(getUserEntity("seller"))

        Mockito.`when`(productRepository.findById(any()))
            .thenReturn(Optional.of(getProductEntity()))

        val response = underTest.purchaseProduct(
            PurchaseRequestDto(1, BigDecimal("50.00")),
            "aarshad"
        )
        assertNotNull(response)
    }

    @Test
    fun updateProduct() {
    }

    @Test
    fun deleteProduct() {
    }


    fun getSellerRequestDto(
        amountAvailable : BigDecimal?,
        cost : BigDecimal?,
        productName : String?
    ) = SellerRequestDto(
        amountAvailable,
        cost,
        productName
    )

    fun getUserEntity(role : String) = UserEntity(1,
        "aarshad",
                "233232",
        BigDecimal("80"),
        role
    )

    fun getProductEntity()  = ProductEntity(1,
        "Mango",
        1,
        BigDecimal("100.00"), BigDecimal("10.00")
    )
}