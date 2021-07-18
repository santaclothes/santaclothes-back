package com.pinocchio.santaclothes.apiserver.controller.dto

import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.service.AnalysisRequestDocument
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class AnalysisRequestForm(
    val clothImage: MultipartFile,
    val labelImages: MultipartFile,
    val clothName: String,
    val clothType: ClothesType,
    val clothColor: ClothesColor,
)

data class AnalysisRequestResult(
    val analysisRequestId: Long,
)

data class AnalysisStatusRequest(
    val status: AnalysisStatus
)

data class NotificationList(
    val userName: String,
    val notificationElements: List<NotificationElement>
)

data class NotificationElement(
    val id: Long,
    val analysisRequestId: Long,
    val clothName: String,
    val requestAt: LocalDateTime,
)

fun AnalysisRequestForm.toAnalysisRequestDocument() =
    AnalysisRequestDocument(
        clothName = clothName,
        clothesColor = clothColor,
        clothesType = clothType,
        clothImage = clothImage,
        labelImage = labelImages
    )
