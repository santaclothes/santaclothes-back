package com.pinocchio.santaclothes.apiserver.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("NOTICE")
data class Notice(
    @JsonIgnore @Id @Column("ID") var id: Long? = null,
    @Column("TITLE") val title: String,
    @Column("HINT") val hint: String,
    @Column("CONTENT") val content: String
)
