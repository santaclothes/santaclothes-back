package com.pinocchio.santaclothes.apiserver.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class AnalysisRequest(
    @JsonIgnore @Id var id: Long? = null,
    val userToken: String,
    @MappedCollection(idColumn = "ID") val cloth: Cloth,
    val status: AnalysisStatus = AnalysisStatus.REQUEST,
    val createdAt: Instant = Instant.now()
)

enum class AnalysisStatus {
    REQUEST,
    CLASSIFIED,
    NOTIFIED,
    DONE,
    FAILED
}