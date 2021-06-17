package com.pinocchio.santaclothes.apiserver.event

import com.pinocchio.santaclothes.apiserver.entity.ImageType

data class AnalysisDoneEvent(
    val clothId: Long,
    val careLabelId: Long,
    val careLabelImageId: Long
)

data class ImageUploadEvent(
    val fileName: String,
    val filePath: String,
    val fileUrl: String,
    val type: ImageType,
    val clothId: Long,
    val userToken: String
)
