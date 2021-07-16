package com.pinocchio.santaclothes.apiserver.event

import org.springframework.web.multipart.MultipartFile

data class CareLabelUpdateCommand(
    val analysisRequestId: Long,
    val careLabelId: Long,
    val careLabelImageId: Long
)

data class NotificationSendCommand(val analysisRequestId: Long)

data class ImageUploadCommand(
    val file: MultipartFile,
    val filePath: String
)

data class AnalysisRequestNotifiedEvent(val analysisRequestId: Long)

data class AnalysisRequestDoneEvent(val analysisRequestId: Long)
