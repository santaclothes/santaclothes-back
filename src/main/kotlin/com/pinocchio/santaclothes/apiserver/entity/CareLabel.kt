package com.pinocchio.santaclothes.apiserver.entity

import com.pinocchio.santaclothes.apiserver.entity.type.*
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table

@Table
data class CareLabel(
    @Id var id: Long? = null,
    val waterType: WaterType,
    val bleachType: BleachType,
    val dryType: DryType,
    val dryCleaning: DryCleaning,
    val ironingType: IroningType,
    @MappedCollection(idColumn = "IMAGE_ID") val images: Set<ImageRef>? = setOf(),
)

@Table("CARE_LABEL_IMAGE_REF")
data class ImageRef(
    var imageId: Long
)
