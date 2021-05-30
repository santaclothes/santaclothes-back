package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("USER")
data class User(
    @Id @Column("TOKEN") var token: String,
    @Column("NAME") val name: String,
    @Column("ACCOUNT_TYPE") val accountType: AccountType
)

enum class AccountType {
    KAKAO,
}
