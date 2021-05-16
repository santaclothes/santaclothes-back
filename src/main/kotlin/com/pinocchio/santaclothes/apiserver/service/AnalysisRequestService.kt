package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.repository.AnalysisRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.time.Instant
import java.util.UUID

@Service
class AnalysisRequestService(
    @Autowired val analysisRequestRepository: AnalysisRequestRepository,
    @Autowired val tokenManager: TokenManager
) {
    fun getById(id: Long): AnalysisRequest = analysisRequestRepository.findById(id).orElseThrow()

    fun save(document: AnalysisRequestDocument): AnalysisRequest {
        // TODO: 파일 업로드
        //       옷 이미지 업로드 후 set
        return analysisRequestRepository.save(
            AnalysisRequest(
                userToken = tokenManager.getUserTokenByAccessToken(document.accessToken),
                cloth = Cloth(name = document.clothName, color = document.clothesColor, type = document.clothesType),
                status = AnalysisStatus.REQUEST,
                createdAt = Instant.now()
            )
        )
    }

    fun setCareLabel(requestId: Long, careLabel: CareLabel): AnalysisRequest =
        getById(requestId).apply { cloth.careLabel = careLabel }
            .run { analysisRequestRepository.save(this) }
}

data class AnalysisRequestDocument(
    val accessToken: UUID,
    val clothImage: File,
    val labelImage: File,
    val clothName: String,
    val clothesType: ClothesType,
    val clothesColor: ClothesColor
)
