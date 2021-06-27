package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.CareLabelIcon
import com.pinocchio.santaclothes.apiserver.entity.*
import com.pinocchio.santaclothes.apiserver.entity.type.*
import com.pinocchio.santaclothes.apiserver.repository.AnalysisRequestRepository
import com.pinocchio.santaclothes.apiserver.repository.AuthorizationTokenRepository
import com.pinocchio.santaclothes.apiserver.repository.ClothRepository
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AnalysisServiceTest(
    @Autowired val sut: AnalysisService,
    @Autowired val userRepository: UserRepository,
    @Autowired val authorizationTokenRepository: AuthorizationTokenRepository,
    @Autowired val clothRepository: ClothRepository,
    @Autowired val imageRepository: ImageRepository,
    @Autowired val analysisRequestRepository: AnalysisRequestRepository,
) : SpringDataTest() {
    @Test
    @Disabled("실제로 테스트에서 알람을 보낼지 결정한 후 활성화")
    fun analysis() {
        val userToken = "test"
        userRepository.insert(User(userToken, "name", AccountType.KAKAO))
        authorizationTokenRepository.save(AuthorizationToken(userToken = userToken, deviceToken = "deviceToken"))

        val cloth = Cloth(
            name = "cloth",
            type = ClothesType.TOP,
            color = ClothesColor.BEIGE,
            userToken = userToken
        )

        val analysisRequest = analysisRequestRepository.save(
            AnalysisRequest(
                userToken = userToken,
                cloth = cloth
            )
        )

        val clothId = analysisRequest.cloth.id!!
        val imageId = imageRepository.save(
            Image(
                fileName = "fileName",
                filePath = "filePath",
                fileUrl = "fileUrl",
                type = ImageType.CARE_LABEL,
                clothId = clothId,
                userToken = userToken
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

        val expectedAnalysisRequest = analysisRequestRepository.findByClothId(clothId).orElseThrow()
        then(expectedAnalysisRequest.status).isEqualTo(AnalysisStatus.CLASSIFIED)

        // TODO: 노티 처리
    }
    // TODO: 노티가 없을 떄 예외 테스트 추가
}
