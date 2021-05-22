package com.pinocchio.santaclothes.apiserver.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Notice(
    @JsonIgnore @Id var id: Long? = null,
    val title: String,
    val hint: String,
    val content: String
)
