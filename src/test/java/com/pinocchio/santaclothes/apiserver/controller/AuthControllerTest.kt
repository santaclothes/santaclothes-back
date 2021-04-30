package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.controller.dto.RegisterRequest
import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.service.UserService
import com.pinocchio.santaclothes.apiserver.test.ApiTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.boot.test.mock.mockito.MockBean


class AuthControllerTest : ApiTest() {
    @MockBean
    lateinit var userService: UserService

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
}
