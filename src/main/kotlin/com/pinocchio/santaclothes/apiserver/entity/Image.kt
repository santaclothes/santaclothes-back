package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
class Image(
    @Id val Id: String,
    val filePath: String,
    val thumbnailPath: String?,
    val imageType: ImageType,
    // TODO: 객체 참조 필요한지 검토
)


enum class ImageType {
    CLOTH,
    CARE_LABEL
}
