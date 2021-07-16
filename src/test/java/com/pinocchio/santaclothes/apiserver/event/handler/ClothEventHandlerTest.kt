package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.Image
import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.entity.type.BleachType
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.entity.type.DryCleaning
import com.pinocchio.santaclothes.apiserver.entity.type.DryType
import com.pinocchio.santaclothes.apiserver.entity.type.IroningType
import com.pinocchio.santaclothes.apiserver.entity.type.WaterType
import com.pinocchio.santaclothes.apiserver.event.CareLabelUpdateCommand
import com.pinocchio.santaclothes.apiserver.repository.AnalysisRequestRepository
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.service.AuthorizationTokenService
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher

class ClothEventHandlerTest @Autowired constructor(
    private val sut: ApplicationEventPublisher,
    private val analysisRequestRepository: AnalysisRequestRepository,
    private val imageRepository: ImageRepository,
) : SpringDataTest() {
    @MockBean
    lateinit var authorizationTokenService: AuthorizationTokenService

    @MockBean
    lateinit var tokenManager: TokenManager

    @Test
    fun analysisDone() {
        val userToken = "userToken"
        val analysisRequest = analysisRequestRepository.save(
            AnalysisRequest(
                userToken = userToken,
                cloth = Cloth(
                    name = "test",
                    userToken = userToken,
                    color = ClothesColor.RED,
                    type = ClothesType.TOP,
                    careLabel = CareLabel(
                        waterType = WaterType.FORBIDDEN_BLOWER_JP,
                        bleachType = BleachType.CL_BLEACH_KR,
                        dryType = DryType.DRY_BLOWER_FORBIDDEN_KR,
                        dryCleaning = DryCleaning.DRY_CLEANING_FORBIDDEN_JP,
                        ironingType = IroningType.IRONING_140_160_FABRIC_KR
                    ),
                )
            )
        )

        val careLabelId = analysisRequest.cloth.careLabel!!.id!!

        val careLabelImageId = imageRepository.save(
            Image(
                fileName = "fileName",
                filePath = "filePath",
                fileUrl = "fileUrl",
                type = ImageType.CARE_LABEL
            )
        ).imageId!!

        given { authorizationTokenService.getByUserToken(userToken) }.willReturn(
            AuthorizationToken(
                userToken = userToken,
                deviceToken = "deviceToken"
            )
        )

        // when
        sut.publishEvent(
            CareLabelUpdateCommand(
                analysisRequestId = analysisRequest.id!!,
                careLabelId = careLabelId,
                careLabelImageId = careLabelImageId
            )
        )

        // then
        val careLabelImage = imageRepository.findById(careLabelImageId).orElseThrow()
        then(careLabelImage.careLabelId).isEqualTo(careLabelId)
    }
}
