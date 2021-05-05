package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.UserToken
import com.pinocchio.santaclothes.apiserver.exception.DatabaseException
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import com.pinocchio.santaclothes.apiserver.repository.UserTokenRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.thenNoException
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.temporal.ChronoUnit

class UserServiceTest(
    @Autowired val sut: UserService,
    @Autowired val userRepository: UserRepository,
    @Autowired val userTokenRepository: UserTokenRepository
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
        sut.register(userToken, "name", AccountType.KAKAO)

        thenNoException().isThrownBy { sut.login(userToken, deviceToken) }
    }

    @Test
    fun loginWithExpiredToken() {
        val userToken = "token"
        val deviceToken = "deviceToken"

        sut.register(userToken, "name", AccountType.KAKAO)
        userTokenRepository.save(
            UserToken(
                userToken = userToken,
                deviceToken = deviceToken,
                expiredAt = Instant.now().minus(1, ChronoUnit.DAYS)
            )
        )

        thenThrownBy { sut.login(userToken, deviceToken) }
            .isExactlyInstanceOf(TokenInvalidException::class.java)
            .matches { (it as TokenInvalidException).reason == ExceptionReason.INVALID_ACCESS_TOKEN }
    }
}
