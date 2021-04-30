package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.test.SpringTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
class UserRepositoryTest(@Autowired val sut: UserRepository) : SpringTest() {
    @Test
    fun save() {
        val token = "token"
        val expected = User(token = token, name = "test", accountType = AccountType.KAKAO)

        sut.insert(expected)

        val actual = sut.findById(token).get()
        then(actual).isEqualTo(expected)
    }
}
