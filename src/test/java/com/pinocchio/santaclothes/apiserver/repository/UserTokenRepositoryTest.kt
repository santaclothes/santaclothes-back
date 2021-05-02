package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.UserToken
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.util.UUID

class UserTokenRepositoryTest(@Autowired val sut: UserTokenRepository) : SpringDataTest() {
    @Test
    fun save() {
        val userToken = UserToken(accessToken = UUID.randomUUID(), refreshToken = UUID.randomUUID())

        val actual = sut.save(userToken)

        then(actual.accessToken).isEqualTo(userToken.accessToken)
        then(actual.refreshToken).isEqualTo(userToken.refreshToken)
        then(actual.createdAt).isBeforeOrEqualTo(Instant.now())
        then(actual.expiredAt).isAfterOrEqualTo(Instant.now())
    }
}
