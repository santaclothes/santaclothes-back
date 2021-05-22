package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("USER")
data class User(
    @Id @Column("token") var token: String,
    val name: String,
    val accountType: AccountType
)

enum class AccountType {
    KAKAO,
}
