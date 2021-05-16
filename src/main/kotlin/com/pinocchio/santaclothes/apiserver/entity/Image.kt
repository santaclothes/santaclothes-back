package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Image(
    @Id var imageId: String? = null,
    val filePath: String,
    val thumbnailPath: String? = null,
    val imageType: ImageType,
    var userToken: String? = null
)


enum class ImageType {
    CLOTH,
    CARE_LABEL
}
