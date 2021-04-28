package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.UserToken
import com.pinocchio.santaclothes.apiserver.test.SpringTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Transactional
class UserTokenRepositoryTest(@Autowired val sut: UserTokenRepository) : SpringTest() {
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
