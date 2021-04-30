package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("USER")
data class User(
    @Id var token: String? = null,
    var name: String,
    var accountType: AccountType,
)

enum class AccountType {
    KAKAO,
}
