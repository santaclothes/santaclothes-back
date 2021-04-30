package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.controller.dto.RegisterRequest
import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.test.ApiTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test


class AuthControllerTest : ApiTest() {
    @Test
    fun register() {
        val registerRequest = RegisterRequest("token", "name", AccountType.KAKAO)

        Given {
            contentType(ContentType.JSON)
            body(registerRequest)
        } When {
            post("/auth/register")
        } Then {
            statusCode(201)
        }
    }
}
