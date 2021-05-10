package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.exception.DatabaseException
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.thenNoException
import org.assertj.core.api.BDDAssertions.thenThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserServiceTest(
    @Autowired val sut: UserService,
    @Autowired val userRepository: UserRepository,
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
}
