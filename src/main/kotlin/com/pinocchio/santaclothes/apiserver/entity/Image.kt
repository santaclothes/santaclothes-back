package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("IMAGE")
data class Image(
    @Id @Column("IMAGE_ID") var imageId: Long? = null,
    @Column("FILE_PATH") val filePath: String,
    @Column("THUMBNAIL_PATH") val thumbnailPath: String? = null,
    @Column("TYPE") val type: ImageType,
    @Column("CLOTH_ID") var clothId: Long? = null,
    @Column("CARE_LABEL_ID") var careLabelId: Long? = null,
    @Column("USER_TOKEN") var userToken: String? = null,
)

enum class ImageType {
    CLOTH,
    CARE_LABEL
}
