package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table
data class Image(
    @Id @Column("image_id") var imageId: Long? = null,
    val filePath: String,
    val thumbnailPath: String? = null,
    val type: ImageType,
    var clothId: Long? = null,
    var careLabelId: Long? = null,
    var userToken: String? = null,
)

enum class ImageType {
    CLOTH,
    CARE_LABEL
}
