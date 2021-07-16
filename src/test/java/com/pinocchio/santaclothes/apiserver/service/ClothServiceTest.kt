package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.type.BleachType
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.entity.type.DryCleaning
import com.pinocchio.santaclothes.apiserver.entity.type.DryType
import com.pinocchio.santaclothes.apiserver.entity.type.IroningType
import com.pinocchio.santaclothes.apiserver.entity.type.WaterType
import com.pinocchio.santaclothes.apiserver.repository.ClothRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ClothServiceTest(
    @Autowired val sut: ClothService,
    @Autowired val clothRepository: ClothRepository,
    @Autowired val userService: UserService
) : SpringDataTest() {

    @Test
    fun incrementCount() {
        // when
        sut.incrementCount()

        then(sut.getCount()).isEqualTo(1L)
    }

    @Test
    fun addCareLabel() {
        // given
        val userToken = "token"
        userService.register(userToken, "name", AccountType.KAKAO)
        val saved = clothRepository.save(
            Cloth(
                name = "cloth1",
                color = ClothesColor.BEIGE,
                type = ClothesType.TOP,
                userToken = userToken
            )
        )

        val clothId = saved.id!!

        val expected = CareLabel(
            waterType = WaterType.FORBIDDEN_BLOWER_JP,
            bleachType = BleachType.CL_BLEACH_KR,
            dryCleaning = DryCleaning.DRY_CLEANING_FORBIDDEN_JP,
            dryType = DryType.DRY_BLOWER_FORBIDDEN_KR,
            ironingType = IroningType.IRONING_140_160_FABRIC_KR
        )

        // when
        sut.addCareLabel(clothId, expected)

        val actual = clothRepository.findById(clothId).orElseThrow().careLabel
        then(actual).isEqualTo(expected)
    }
}
