package com.mvp.vendingmachine.vending.service

import com.mvp.vendingmachine.common.ErrorCodes
import com.mvp.vendingmachine.common.NO_FOR_MULTIPLE_OF
import com.mvp.vendingmachine.common.exception.ApplicationException
import com.mvp.vendingmachine.common.utils.DepositUtils
import com.mvp.vendingmachine.user.entity.dto.ProductEntity
import com.mvp.vendingmachine.user.repository.UserRepository
import com.mvp.vendingmachine.vending.entity.dto.*
import com.mvp.vendingmachine.vending.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.stream.Collectors


@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun addProduct(sellerRequestDto: SellerRequestDto, userName : String) : SellerResponseDto {
        val userEntity = userRepository.findByUserName(userName)
        userEntity?: throw ApplicationException(ErrorCodes.USER_NOT_EXIST, "user not exist")

        validateAmountMultipleOfNumber(sellerRequestDto.cost!!, NO_FOR_MULTIPLE_OF)

        val productEntity = productRepository.save(ProductEntity( // TODO creaet a mapper class
            productName = sellerRequestDto.productName.toString(),
            sellerId = userEntity.id,
            availableAmount = sellerRequestDto.amountAvailable,
            cost = sellerRequestDto.cost
        ))
        return SellerResponseDto(productEntity.id, productEntity.productName, "Product added by seller id ${userEntity.userName}")
    }

    fun validateAmountMultipleOfNumber(amount : BigDecimal, number : String) {
        val remainder = amount?.remainder(BigDecimal(number))
        if (BigDecimal.ZERO.compareTo(remainder) != 0) throw ApplicationException(ErrorCodes.DEPOSIT_AMOUNT_NOT_VALID)
    }

    fun getProducts(userName : String) : List<SellerResponseDto> {
        val userEntity = userRepository.findByUserName(userName)
        userEntity?: throw ApplicationException(ErrorCodes.USER_NOT_EXIST, "user not exist")
        val productEntities = productRepository.findBySellerId(userEntity?.id!!)
        productEntities?: throw ApplicationException(ErrorCodes.USER_NOT_EXIST, "user not exist")

        val response = productEntities.stream().map {
            SellerResponseDto(it.id, it.productName, null)
        }.collect(Collectors.toList())
        return response
    }

    fun purchaseProduct(purchaseRequestDto: PurchaseRequestDto, userName : String) : PurchaseResponseDto {
        val userEntity = userRepository.findByUserName(userName)
        userEntity?: throw ApplicationException(ErrorCodes.USER_NOT_EXIST, "user not exist")

        val product = productRepository.findById(purchaseRequestDto.productId)
        if(product.isEmpty) throw ApplicationException(ErrorCodes.NOT_PRODUCT_FOUND, "No product found for product id ${purchaseRequestDto.productId}")

        if(purchaseRequestDto.amount!! > userEntity.deposit)
            throw ApplicationException(ErrorCodes.DEPOSIT_AMOUNT_NOT_VALID, "you have less amount in deposit")

        validateAmountMultipleOfNumber(purchaseRequestDto.amount, NO_FOR_MULTIPLE_OF)

        val remainingAmount = userEntity.deposit?.minus(purchaseRequestDto.amount)

        log.debug("remaining amount: $remainingAmount")
        validateAmountMultipleOfNumber(remainingAmount!!, NO_FOR_MULTIPLE_OF)

        // this is for getting purchase amounts
        val noOfProducts = purchaseRequestDto.amount.divide(product.get().cost)
        val totalPurchasedAmount = noOfProducts.multiply(product.get().cost)
        product.get().availableAmount = product.get().availableAmount?.minus(totalPurchasedAmount)
        productRepository.save(product.get())

        val returnedCoins = DepositUtils.coinChange(remainingAmount.toInt())

        return PurchaseResponseDto(returnedCoins, totalPurchasedAmount,
            Product(product.get().id, product.get().productName)
        )
    }

    fun updateProduct(sellerRequestDto: SellerRequestDto, productId : Long, userName : String) : SellerResponseDto {
        val userEntity = userRepository.findByUserName(userName)
        userEntity?: throw ApplicationException(ErrorCodes.USER_NOT_EXIST, "user not exist")

        val product = productRepository.findById(productId)
        if(product.isEmpty) throw ApplicationException(ErrorCodes.NOT_PRODUCT_FOUND, "No product found for product id ${productId}")

        product.get().productName = sellerRequestDto.productName
        product.get().cost = sellerRequestDto.cost

        productRepository.save(product.get())
        return SellerResponseDto(productId, sellerRequestDto.productName, "Product updated")
    }

    fun deleteProduct(productId : Long, userName : String) : SellerResponseDto{
        val userEntity = userRepository.findByUserName(userName)
        userEntity?: throw ApplicationException(ErrorCodes.USER_NOT_EXIST, "user not exist")

        val product = productRepository.findById(productId)
        if(product.isEmpty) throw ApplicationException(ErrorCodes.NOT_PRODUCT_FOUND, "No product found for product id ${productId}")

        productRepository.delete(product.get())
        return SellerResponseDto(productId, null,"Product Deleted")
    }

}