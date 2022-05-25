package com.mvp.vendingmachine.user.repository

import com.mvp.vendingmachine.user.entity.dto.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUserName(userName : String) : UserEntity?
}