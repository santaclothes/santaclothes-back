package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.controller.dto.LoginRequest
import com.pinocchio.santaclothes.apiserver.controller.dto.RefreshRequest
import com.pinocchio.santaclothes.apiserver.controller.dto.RegisterRequest
import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.exception.DatabaseException
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.service.UserService
import com.pinocchio.santaclothes.apiserver.test.ApiTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.UUID


class AuthControllerTest : ApiTest() {
    @MockBean
    lateinit var userService: UserService

    @MockBean
    lateinit var tokenManager: TokenManager

    @Test
    fun register() {
        val token = "token"
        val name = "name"
        val accountType = AccountType.KAKAO

        val registerRequest = RegisterRequest(token, name, accountType)

        Given {
            contentType(ContentType.JSON)
            body(registerRequest)
        } When {
            post("/auth/register")
        } Then {
            statusCode(201)
        }

        verify(userService, times(1)).register(token, name, accountType)
    }

    @Test
    fun registerExistUser() {
        val token = "token"
        val name = "name"
        val accountType = AccountType.KAKAO

        val registerRequest = RegisterRequest(token, name, accountType)

        BDDMockito.given(userService.register(token, name, accountType))
            .willThrow(DatabaseException(ExceptionReason.DUPLICATE_ENTITY))

        Given {
            contentType(ContentType.JSON)
            body(registerRequest)
        } When {
            post("/auth/register")
        } Then {
            statusCode(409)
        }
    }

    @Test
    fun login() {
        val userToken = "token"
        val deviceToken = "deviceToken"
        val loginRequest = LoginRequest(userToken, deviceToken)

        Given {
            contentType(ContentType.JSON)
            body(loginRequest)
        } When {
            post("/auth/login")
        } Then {
            statusCode(200)
        }

        verify(userService, times(1)).login(userToken, deviceToken)
    }

    @Test
    fun loginWithNoTokenThrows() {
        val userToken = "token"
        val deviceToken = "deviceToken"
        val loginRequest = LoginRequest(userToken, deviceToken)

        BDDMockito.given(
            userService.login(
                userToken,
                deviceToken
            )
        ).willThrow(TokenInvalidException(ExceptionReason.USER_TOKEN_NOT_EXISTS))

        Given {
            contentType(ContentType.JSON)
            body(loginRequest)
        } When {
            post("/auth/login")
        } Then {
            statusCode(400)
        }
    }

    @Test
    fun refreshAccessToken() {
        val refreshToken = UUID.randomUUID()
        val refreshRequest = RefreshRequest(refreshToken)

        Given {
            contentType(ContentType.JSON)
            body(refreshRequest)
        } When {
            put("/auth/accessToken")
        } Then {
            statusCode(200)
        }

        verify(tokenManager, times(1)).refreshAccessToken(refreshToken)
    }

    @Test
    fun refreshAccessTokenIsExpiredThrows() {
        val refreshToken = UUID.randomUUID()
        val refreshRequest = RefreshRequest(refreshToken)

        BDDMockito.given(tokenManager.refreshAccessToken(refreshToken))
            .willThrow(TokenInvalidException(ExceptionReason.INVALID_REFRESH_TOKEN))

        Given {
            contentType(ContentType.JSON)
            body(refreshRequest)
        } When {
            put("/auth/accessToken")
        } Then {
            statusCode(400)
        }
    }
}
