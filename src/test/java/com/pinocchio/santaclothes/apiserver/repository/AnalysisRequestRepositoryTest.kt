package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AnalysisRequestRepositoryTest(
    @Autowired val sut: AnalysisRequestRepository,
    @Autowired val userRepository: UserRepository
) : SpringDataTest() {
    @Test
    fun save() {
        // given
        val saved = userRepository.insert(User(token = "token", name = "test", accountType = AccountType.KAKAO)).run {
            AnalysisRequest(
                userToken = token,
                cloth = Cloth(name = "티셔츠", color = ClothesColor.BEIGE, type = ClothesType.TOP)
            )
        }

        // when
        val actual = sut.save(saved)

        // then
        with(actual) {
            then(userToken).isEqualTo(saved.userToken)
            then(status).isEqualTo(saved.status)
            with(actual.cloth) {
                then(name).isEqualTo(saved.cloth.name)
                then(color).isEqualTo(saved.cloth.color)
                then(type).isEqualTo(saved.cloth.type)
            }
        }
    }
}
