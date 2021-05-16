package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import com.pinocchio.santaclothes.apiserver.exception.DatabaseException
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import com.pinocchio.santaclothes.apiserver.repository.AuthorizationTokenRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.thenNoException
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.temporal.ChronoUnit

class UserServiceTest(
    @Autowired val sut: UserService,
    @Autowired val userRepository: UserRepository,
    @Autowired val authorizationTokenRepository: AuthorizationTokenRepository
) : SpringDataTest() {
    @Test
    fun register() {
        val userToken = "token"

        sut.register(userToken, "name", AccountType.KAKAO)

        thenNoException().isThrownBy {
            userRepository.findById(userToken).get()
        }
    }

    @Test
    fun registerTwice() {
        val userToken = "token"
        sut.register(userToken, "name", AccountType.KAKAO)

        thenThrownBy { sut.register(userToken, "name", AccountType.KAKAO) }
            .isExactlyInstanceOf(DatabaseException::class.java)
            .matches { (it as DatabaseException).reason == ExceptionReason.DUPLICATE_ENTITY }
    }

    @Test
    fun login() {
        val userToken = "token"
        val deviceToken = "deviceToken"
        userRepository.insert(User(userToken, "name", AccountType.KAKAO))

        thenNoException().isThrownBy { sut.login(userToken, deviceToken) }
    }

    @Test
    fun loginWithExpiredToken() {
        val userToken = "token"
        val deviceToken = "deviceToken"

        userRepository.insert(User(userToken, "name", AccountType.KAKAO))
        authorizationTokenRepository.save(
            AuthorizationToken(
                userToken = userToken,
                deviceToken = deviceToken,
                expiredAt = Instant.now().minus(1, ChronoUnit.DAYS)
            )
        )

        thenThrownBy { sut.login(userToken, deviceToken) }
            .isExactlyInstanceOf(TokenInvalidException::class.java)
            .matches { (it as TokenInvalidException).reason == ExceptionReason.INVALID_ACCESS_TOKEN }
    }

    @Test
    fun findByAccessToken() {
        val userToken = "token"
        val deviceToken = "deviceToken"

        userRepository.insert(User(userToken, "name", AccountType.KAKAO))
        authorizationTokenRepository.save(AuthorizationToken(userToken = userToken, deviceToken = deviceToken)).apply {
            val expected = sut.findByAccessToken(accessToken)
            then(expected).isNotEmpty
        }
    }
}
