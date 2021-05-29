package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.CareLabelIcon
import com.pinocchio.santaclothes.apiserver.entity.*
import com.pinocchio.santaclothes.apiserver.entity.type.*
import com.pinocchio.santaclothes.apiserver.repository.ClothRepository
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AnalysisServiceTest(
    @Autowired val sut: AnalysisService,
    @Autowired val userRepository: UserRepository,
    @Autowired val clothRepository: ClothRepository,
    @Autowired val imageRepository: ImageRepository
) : SpringDataTest() {
    @Test
    fun analysis() {
        userRepository.insert(
            User("test", "name", AccountType.KAKAO)
        )
        val cloth = clothRepository.save(
            Cloth(
                name = "cloth",
                type = ClothesType.TOP,
                color = ClothesColor.BEIGE,
                userToken = "test"
            )
        )

        val clothId = cloth.id!!
        val imageId = imageRepository.save(
            Image(
                filePath = "filePath",
                type = ImageType.CARE_LABEL,
                clothId = clothId
            )
        ).imageId!!

        val careLabelIcon = CareLabelIcon(
            waterType = WaterType.FORBIDDEN_BLOWER_JP,
            bleachType = BleachType.CL_BLEACH_KR,
            dryType = DryType.DRY_BLOWER_FORBIDDEN_KR,
            dryCleaning = DryCleaning.DRY_CLEANING_FORBIDDEN_JP,
            ironingType = IroningType.IRONING_140_160_FABRIC_KR
        )

        sut.analysis(imageId, careLabelIcon)

        val actualImage = imageRepository.findById(imageId).orElseThrow()
        val actualCloth = clothRepository.findById(clothId).orElseThrow()
        val actualCareLabel = actualCloth.careLabel
        then(actualImage.careLabelId).isEqualTo(actualCareLabel!!.id)
        with(actualCareLabel) {
            then(waterType).isEqualTo(careLabelIcon.waterType)
            then(bleachType).isEqualTo(careLabelIcon.bleachType)
            then(dryType).isEqualTo(careLabelIcon.dryType)
            then(dryCleaning).isEqualTo(careLabelIcon.dryCleaning)
            then(ironingType).isEqualTo(careLabelIcon.ironingType)
        }
    }
}