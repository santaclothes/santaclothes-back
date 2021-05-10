package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Notice(
    @Id var id: Long? = null,
    val title: String,
    val hint: String,
    val content: String
)
