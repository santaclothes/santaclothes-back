package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.exception.DatabaseException
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.repository.UserTokenRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.thenNoException
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.temporal.ChronoUnit

class UserServiceTest(
    @Autowired val sut: UserService,
    @Autowired val userTokenRepository: UserTokenRepository
) : SpringDataTest() {
    @Test
    @Disabled("회원가입과 로그인 분리하여 수정해야 함")
    fun register() {
        val token = "token"

        sut.register(token, "name", AccountType.KAKAO)

        val actualToken = userTokenRepository.findFirstByUserTokenOrderByCreatedAtDesc(token)
        then(actualToken).isNotEmpty
    }

    @Test
    fun registerTwice() {
        val token = "token"
        sut.register(token, "name", AccountType.KAKAO)

        thenThrownBy { sut.register(token, "name", AccountType.KAKAO) }
            .isExactlyInstanceOf(DatabaseException::class.java)
            .matches { (it as DatabaseException).reason == ExceptionReason.DUPLICATE_ENTITY }
    }

    @Test
    fun login() {
        val token = "token"
        val deviceToken = "deviceToken"
        sut.register(token, "name", AccountType.KAKAO)

        thenNoException().isThrownBy { sut.login(token, deviceToken) }
    }

    @Test
    @Disabled("회원가입과 로그인 분리하여 수정해야 함")
    fun loginWithExpiredToken() {
        val token = "token"
        val deviceToken = "deviceToken"

        sut.register(token, "name", AccountType.KAKAO)
        userTokenRepository.findFirstByUserTokenOrderByCreatedAtDesc(token).get()
            .copy(expiredAt = Instant.now().minus(1, ChronoUnit.DAYS))
            .apply {
                userTokenRepository.save(this)
            }

        thenThrownBy { sut.login(token, deviceToken) }
            .isExactlyInstanceOf(TokenInvalidException::class.java)
            .matches { (it as TokenInvalidException).reason == ExceptionReason.INVALID_ACCESS_TOKEN }
    }

    @Test
    fun refresh() {
        val token = "token"
        val deviceToken = "deviceToken"

        sut.register(token, "name", AccountType.KAKAO)
        val authentication = sut.login(token, deviceToken)
        val refreshToken = authentication.refreshToken

        val actual = sut.refresh(refreshToken)

        then(actual.id).isNotEqualTo(authentication.id)
        then(actual.refreshToken).isEqualTo(refreshToken)
        then(actual.accessToken).isNotEqualTo(authentication.accessToken)
        then(actual.expiredAt).isNotEqualTo(authentication.expiredAt)
    }

    @Test
    fun validateAccessToken() {
        val token = "token"
        val deviceToken = "deviceToken"

        sut.register(token, "name", AccountType.KAKAO)
        val authentication = sut.login(token, deviceToken)

        thenNoException().isThrownBy { sut.validateAccessToken(authentication.accessToken) }
    }

    @Test
    fun validateAccessTokenIsExpiredThrows() {
        val token = "token"
        val deviceToken = "deviceToken"

        sut.register(token, "name", AccountType.KAKAO)
        val authentication = sut.login(token, deviceToken)
        val now = Instant.now()
        userTokenRepository.save(authentication.copy(expiredAt = now))

        thenThrownBy { sut.validateAccessToken(authentication.accessToken) }
            .isExactlyInstanceOf(TokenInvalidException::class.java)
    }
}
