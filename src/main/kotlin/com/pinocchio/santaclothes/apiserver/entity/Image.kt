package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
class Image(
    @Id val Id: String,
    val filePath: String,
    val thumbnailPath: String?,
    val imageType: ImageType,
    val cloth: Cloth?,
    val careLabel: CareLabel?
)


enum class ImageType {
    CLOTH,
    CARE_LABEL
}
