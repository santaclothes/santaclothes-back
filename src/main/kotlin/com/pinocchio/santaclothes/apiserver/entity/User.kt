package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("USER")
data class User(
    @Id var token: UUID? = null,
    var name: String,
    var accountType: AccountType,
)

enum class AccountType {
    KAKAO,
}
