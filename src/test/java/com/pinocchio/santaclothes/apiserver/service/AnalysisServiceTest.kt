package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.CareLabelIcon
import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.Image
import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.entity.type.BleachType
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.entity.type.DryCleaning
import com.pinocchio.santaclothes.apiserver.entity.type.DryType
import com.pinocchio.santaclothes.apiserver.entity.type.IroningType
import com.pinocchio.santaclothes.apiserver.entity.type.WaterType
import com.pinocchio.santaclothes.apiserver.repository.AnalysisRequestRepository
import com.pinocchio.santaclothes.apiserver.repository.ClothRepository
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher

class AnalysisServiceTest @Autowired constructor(
    private val sut: AnalysisService,
    private val userRepository: UserRepository,
    private val clothRepository: ClothRepository,
    private val imageRepository: ImageRepository,
    private val analysisRequestRepository: AnalysisRequestRepository,
) : SpringDataTest() {
    @MockBean
    lateinit var publisher: ApplicationEventPublisher

    @Test
    fun analysis() {
        // given
        val userToken = "test"
        userRepository.insert(User(userToken, "name", AccountType.KAKAO))

        val cloth = Cloth(
            name = "cloth",
            type = ClothesType.TOP,
            color = ClothesColor.BEIGE,
            userToken = userToken
        )

        val analysisRequest = analysisRequestRepository.save(
            AnalysisRequest(userToken = userToken, cloth = cloth)
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

        // when
        sut.analysis(imageId, careLabelIcon)

        val actualCloth = clothRepository.findById(clothId).orElseThrow()
        val actualCareLabel = actualCloth.careLabel
        then(actualCareLabel!!.clothId).isEqualTo(clothId)
    }
}
