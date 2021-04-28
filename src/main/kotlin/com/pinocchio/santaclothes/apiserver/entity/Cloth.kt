package com.pinocchio.santaclothes.apiserver.entity

import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Cloth(
    @Id var id: Long? = null,
    val name: String,
    val color: ClothesColor,
    val type: ClothesType,
    val careLabels: Set<CareLabel> = setOf()
)
