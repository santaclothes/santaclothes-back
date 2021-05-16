package com.pinocchio.santaclothes.apiserver.authorization

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import com.pinocchio.santaclothes.apiserver.repository.UserTokenRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.thenNoException
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.temporal.ChronoUnit

class TokenManagerTest(
    @Autowired val sut: TokenManager,
    @Autowired val userRepository: UserRepository,
    @Autowired val userTokenRepository: UserTokenRepository
) : SpringDataTest() {
    @Test
    fun refreshAccessToken() {
        val userToken = "token"
        val deviceToken = "deviceToken"

        userRepository.insert(User(token = userToken, name = "test", accountType = AccountType.KAKAO))
        userTokenRepository.save(AuthorizationToken(userToken = userToken, deviceToken = deviceToken))
            .run {
                val actual = sut.refreshAccessToken(refreshToken)

                then(actual.id).isNotEqualTo(id)
                then(actual.refreshToken).isEqualTo(refreshToken)
                then(actual.accessToken).isNotEqualTo(accessToken)
                then(actual.expiredAt).isNotEqualTo(expiredAt)
            }
    }

    @Test
    fun validateAccessToken() {
        val userToken = "token"
        val deviceToken = "deviceToken"

        userRepository.insert(User(token = userToken, name = "test", accountType = AccountType.KAKAO))
        userTokenRepository.save(AuthorizationToken(userToken = userToken, deviceToken = deviceToken))
            .run {
                thenNoException().isThrownBy { sut.validateAccessToken(accessToken) }
            }
    }

    @Test
    fun validateAccessTokenIsExpiredThrows() {
        val userToken = "token"
        val deviceToken = "deviceToken"

        userRepository.insert(User(token = userToken, name = "test", accountType = AccountType.KAKAO))
        userTokenRepository.save(AuthorizationToken(userToken = userToken, deviceToken = deviceToken, expiredAt = Instant.now()))
            .run {
                thenThrownBy { sut.validateAccessToken(accessToken) }
                    .isExactlyInstanceOf(TokenInvalidException::class.java)
            }
    }

    @Test
    fun acquireAccessToken() {
        val userToken = "token"
        val deviceToken = "deviceToken"
        userRepository.insert(User(token = userToken, name = "test", accountType = AccountType.KAKAO))

        thenNoException().isThrownBy { sut.acquireAccessToken(userToken, deviceToken) }
    }

    @Test
    fun acquireExpiredAccessTokenThrows() {
        val userToken = "token"
        val deviceToken = "deviceToken"
        userRepository.insert(User(token = userToken, name = "test", accountType = AccountType.KAKAO))

        userTokenRepository.save(
            AuthorizationToken(
                userToken = userToken,
                deviceToken = deviceToken,
                expiredAt = Instant.now().minus(1, ChronoUnit.DAYS)
            )
        )

        thenThrownBy { sut.acquireAccessToken(userToken, deviceToken) }
            .isExactlyInstanceOf(TokenInvalidException::class.java)
            .matches { (it as TokenInvalidException).reason == ExceptionReason.INVALID_ACCESS_TOKEN }
    }

    @Test
    fun acquireAccessTokenReturnsCached() {
        val userToken = "token"
        val deviceToken = "deviceToken"
        userRepository.insert(User(token = userToken, name = "test", accountType = AccountType.KAKAO))
        sut.acquireAccessToken(userToken, deviceToken).run {
            userTokenRepository.save(this.copy(expiredAt = Instant.now().minus(1, ChronoUnit.DAYS)))
        }

        thenNoException().isThrownBy { sut.acquireAccessToken(userToken, deviceToken) }
    }

    @Test
    fun acquireAccessTokenAfterRefreshTokenOverwriteCache() {
        val userToken = "token"
        val deviceToken = "deviceToken"
        userRepository.insert(User(token = userToken, name = "test", accountType = AccountType.KAKAO))
        val token = sut.acquireAccessToken(userToken, deviceToken)
        val refreshedToken = sut.refreshAccessToken(token.refreshToken)

        val actual = sut.acquireAccessToken(userToken, deviceToken)

        then(actual).isNotEqualTo(token)
        then(actual).isEqualTo(refreshedToken)
    }
}
