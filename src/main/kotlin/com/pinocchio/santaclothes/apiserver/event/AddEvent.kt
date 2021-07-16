package com.pinocchio.santaclothes.apiserver.event

import com.pinocchio.santaclothes.apiserver.entity.ImageType

data class CareLabelUpdateCommand(
    val analysisRequestId: Long,
    val careLabelId: Long,
    val careLabelImageId: Long
)

data class NotificationSendCommand(val analysisRequestId: Long)

data class ImageUploadEvent(
    val fileName: String,
    val filePath: String,
    val fileUrl: String,
    val type: ImageType,
    val clothId: Long,
    val userToken: String
)

data class AnalysisRequestNotifiedEvent(val analysisRequestId: Long)

data class AnalysisRequestDoneEvent(val analysisRequestId: Long)
