package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.event.AnalysisRequestDoneEvent
import com.pinocchio.santaclothes.apiserver.event.AnalysisRequestNotifiedEvent
import com.pinocchio.santaclothes.apiserver.repository.AnalysisRequestRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.UUID

@Service
class AnalysisRequestService(
    private val analysisRequestRepository: AnalysisRequestRepository,
    private val imageService: ImageService,
    private val authorizationTokenService: AuthorizationTokenService,
    private val publisher: ApplicationEventPublisher,
) {
    fun getById(id: Long): AnalysisRequest = analysisRequestRepository.findById(id).orElseThrow()

    fun getByClothId(clothId: Long): AnalysisRequest = analysisRequestRepository.findByClothId(clothId).orElseThrow()

    fun getByUserToken(userToken: String): List<AnalysisRequest> = analysisRequestRepository.findByUserToken(userToken)

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun save(
        accessToken: UUID,
        document: AnalysisRequestDocument
    ): AnalysisRequest {
        val userToken = authorizationTokenService.getByAccessToken(accessToken).userToken
        val saved = analysisRequestRepository.save(
            AnalysisRequest(
                userToken = userToken,
                cloth = Cloth(
                    name = document.clothName,
                    color = document.clothesColor,
                    type = document.clothesType,
                    userToken = userToken
                ),
                status = AnalysisStatus.REQUEST,
                createdAt = Instant.now()
            )
        )

        val clothId = saved.cloth.id!!
        imageService.upload(
            file = document.clothImage,
            type = ImageType.CLOTH,
            userToken = userToken,
            clothId = clothId
        )
        imageService.upload(
            file = document.labelImage,
            type = ImageType.CARE_LABEL,
            userToken = userToken,
            clothId = clothId
        )
        return saved
    }

    fun withStatus(id: Long, status: AnalysisStatus): AnalysisRequest = analysisRequestRepository.save(
        analysisRequestRepository.findById(id).orElseThrow()
            .also {
                when (it.status) { // 이전 상태
                    AnalysisStatus.NOTIFIED -> publisher.publishEvent(AnalysisRequestNotifiedEvent(it.id!!))
                    else -> Unit
                }

                if (it.status.ordinal <= status.ordinal) {
                    it.status = status
                }

                when (status) { // 변경 상태
                    AnalysisStatus.REPORTED -> {
                        it.cloth.careLabel?.clothId = null
                        it.cloth.careLabel = null
                    }
                    AnalysisStatus.DONE -> publisher.publishEvent(AnalysisRequestDoneEvent(it.id!!))
                    else -> Unit
                }
            }
    )
}

data class AnalysisRequestDocument(
    val clothImage: MultipartFile,
    val labelImage: MultipartFile,
    val clothName: String,
    val clothesType: ClothesType,
    val clothesColor: ClothesColor,
)
