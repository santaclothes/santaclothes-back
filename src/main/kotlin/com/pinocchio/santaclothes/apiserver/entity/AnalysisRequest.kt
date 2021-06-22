package com.pinocchio.santaclothes.apiserver.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("ANALYSIS_REQUEST")
data class AnalysisRequest(
    @JsonIgnore @Id @Column("ID") var id: Long? = null,
    @Column("USER_TOKEN") val userToken: String,
    @MappedCollection(idColumn = "ANALYSIS_REQUEST_ID") var cloth: Cloth,
    @Column("STATUS") var status: AnalysisStatus = AnalysisStatus.REQUEST,
    @Column("CREATED_AT") val createdAt: Instant = Instant.now()
)

// TODO: 이벤트로 상태 비동기적으로 업데이트
enum class AnalysisStatus {
    REQUEST,
    CLASSIFIED,
    NOTIFIED,
    DONE,
    DELETED,
    FAILED
}
