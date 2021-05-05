package com.pinocchio.santaclothes.apiserver.authorization

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.entity.UserToken
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
        userTokenRepository.save(UserToken(userToken = userToken, deviceToken = deviceToken))
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
        userTokenRepository.save(UserToken(userToken = userToken, deviceToken = deviceToken))
            .run {
                thenNoException().isThrownBy { sut.validateAccessToken(accessToken) }
            }
    }

    @Test
    fun validateAccessTokenIsExpiredThrows() {
        val userToken = "token"
        val deviceToken = "deviceToken"

        userRepository.insert(User(token = userToken, name = "test", accountType = AccountType.KAKAO))
        userTokenRepository.save(UserToken(userToken = userToken, deviceToken = deviceToken, expiredAt = Instant.now()))
            .run {
                thenThrownBy { sut.validateAccessToken(accessToken) }
                    .isExactlyInstanceOf(TokenInvalidException::class.java)
            }
    }
}
