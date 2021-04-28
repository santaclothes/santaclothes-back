package com.pinocchio.santaclothes.apiserver.controller.dto

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import java.util.UUID

data class RegisterRequest(val userToken: UUID, val name: String, val accountType: AccountType)

data class LoginRequest(val userToken: UUID)

data class RefreshRequest(val refreshToken: UUID)
