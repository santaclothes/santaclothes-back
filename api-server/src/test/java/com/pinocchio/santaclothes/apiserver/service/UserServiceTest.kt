package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.exception.DatabaseException
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.repository.UserTokenRepository
import com.pinocchio.santaclothes.apiserver.test.SpringTest
import org.assertj.core.api.BDDAssertions.then
import org.assertj.core.api.BDDAssertions.thenNoException
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Transactional
class UserServiceTest(
    @Autowired val sut: UserService,
    @Autowired val userTokenRepository: UserTokenRepository
) : SpringTest() {
    @Test
    fun register() {
        val token = UUID.randomUUID()

        sut.register(token, "name", AccountType.KAKAO)

        val actualToken = userTokenRepository.findByUserToken(token)
        then(actualToken).isNotEmpty
    }

    @Test
    fun registerTwice() {
        val token = UUID.randomUUID()
        sut.register(token, "name", AccountType.KAKAO)

        thenThrownBy { sut.register(token, "name", AccountType.KAKAO) }
            .isExactlyInstanceOf(DatabaseException::class.java)
            .matches { (it as DatabaseException).reason == ExceptionReason.DUPLICATE_ENTITY }
    }

    @Test
    fun login() {
        val token = UUID.randomUUID()
        val authentication = sut.register(token, "name", AccountType.KAKAO)

        thenNoException().isThrownBy { sut.login(authentication.accessToken) }
    }

    @Test
    fun loginWithExpiredToken() {
        val token = UUID.randomUUID()
        val authentication = sut.register(token, "name", AccountType.KAKAO)
        val copy = authentication.copy(expiredAt = Instant.now().minus(1, ChronoUnit.DAYS))
        userTokenRepository.save(copy)

        thenThrownBy { sut.login(authentication.accessToken) }
            .isExactlyInstanceOf(TokenInvalidException::class.java)
            .matches { (it as TokenInvalidException).reason == ExceptionReason.INVALID_ACCESS_TOKEN }
    }
}
