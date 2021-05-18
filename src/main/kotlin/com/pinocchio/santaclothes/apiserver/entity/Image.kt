package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Image(
    @Id var imageId: Long? = null,
    val filePath: String,
    val thumbnailPath: String? = null,
    val imageType: ImageType,
    val imageStatus: ImageStatus = ImageStatus.PROCESS,
    var userToken: String? = null
)

enum class ImageStatus{
    PROCESS,
    DONE
}

enum class ImageType {
    CLOTH,
    CARE_LABEL
}
