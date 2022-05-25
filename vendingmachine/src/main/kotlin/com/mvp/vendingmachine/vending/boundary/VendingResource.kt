package com.mvp.vendingmachine.vending.boundary

import com.mvp.vendingmachine.user.service.UserService
import com.mvp.vendingmachine.vending.entity.dto.*
import com.mvp.vendingmachine.vending.service.ProductService
import com.mvp.vendingmachine.web.annotation.AllowedRoles
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vending")
class VendingResource(
    private val productService: ProductService,
    private val userService: UserService) {


    @PostMapping(value = ["/product"])
    @AllowedRoles(roles = ["seller"])
    fun addProduct(@RequestBody sellerRequestDto: SellerRequestDto) : SellerResponseDto {
        val principle = SecurityContextHolder.getContext().authentication.principal
        return productService.addProduct(sellerRequestDto, principle.toString())
    }

    @GetMapping(value = ["/products"])
    fun getProducts() : List<SellerResponseDto> {
        val principle = SecurityContextHolder.getContext().authentication.principal
        return productService.getProducts(principle.toString())
    }

    @PutMapping(value = ["/product/{productId}"])
    @AllowedRoles(roles = ["seller"])
    fun updateProduct(@RequestBody sellerRequestDto: SellerRequestDto, @RequestParam productId : Long) : SellerResponseDto?  {
        val principle = SecurityContextHolder.getContext().authentication.principal
        return productService.updateProduct(sellerRequestDto, productId, principle.toString())
    }

    @DeleteMapping(value = ["/product/{productId}"])
    @AllowedRoles(roles = ["seller"])
    fun deleteProduct(@RequestBody sellerRequestDto: SellerRequestDto, @RequestParam productId : Long) : SellerResponseDto?  {
        val principle = SecurityContextHolder.getContext().authentication.principal
        return productService.deleteProduct(productId, principle.toString())
    }


    @PostMapping(value = ["/buy"])
    @AllowedRoles(roles = ["buyer"])
    fun buyProduct(@RequestBody purchaseRequestDto: PurchaseRequestDto) : PurchaseResponseDto {
        val principle = SecurityContextHolder.getContext().authentication.principal
        return productService.purchaseProduct(purchaseRequestDto, principle.toString())
    }

    @PostMapping(value = ["/deposit"])
    @AllowedRoles(roles = ["buyer"])
    fun deposit(@RequestBody depositRequestDto: DepositRequestDto) : DepositResponseDto {
        val principle = SecurityContextHolder.getContext().authentication.principal // TODO add that in interceptor
        return userService.updateDepositAmount(principle.toString(), depositRequestDto)
    }

    @PostMapping(value = ["/reset"])
    @AllowedRoles(roles = ["buyer"])
    fun reset(@RequestBody buyerRequestDto: BuyerRequestDto) : BuyerResponseDto {
        return BuyerResponseDto(true) // TODO ipmelmentation
    }

}