package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.*
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.repository.AnalysisRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.Instant
import java.util.UUID

@Service
class AnalysisRequestService(
    @Autowired val analysisRequestRepository: AnalysisRequestRepository,
    @Autowired val imageService: ImageService,
    @Autowired val authorizationTokenService: AuthorizationTokenService,
) {
    fun getById(id: Long): AnalysisRequest = analysisRequestRepository.findById(id).orElseThrow()

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun save(document: AnalysisRequestDocument): AnalysisRequest {
        // TODO: 파일 업로드
        //       옷 이미지 업로드 후 set

        val userToken = authorizationTokenService.getByAccessToken(document.accessToken).userToken
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

        val clothImage = Image(
            filePath = document.clothImage.path,
            type = ImageType.CLOTH,
            clothId = saved.cloth.id,
            userToken = saved.userToken
        )

        val labelImage = Image(
            filePath = document.labelImage.path,
            type = ImageType.CARE_LABEL,
            clothId = saved.cloth.id,
            userToken = saved.userToken
        )

        imageService.saveAll(listOf(clothImage, labelImage))
        return saved
    }
}

data class AnalysisRequestDocument(
    val accessToken: UUID,
    val clothImage: File,
    val labelImage: File,
    val clothName: String,
    val clothesType: ClothesType,
    val clothesColor: ClothesColor,
)