package com.pinocchio.santaclothes.apiserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("NOTIFICAITON")
data class Notification(
    @Id @Column("ID") var id: Long? = null,
    @Column("USER_TOKEN") val userToken: String,
    @Column("ANALYSIS_REQUEST_ID") var analysisRequestId: Long,
    @Column("CATEGORY") val category: NotificationCategory,
    @Column("NEW") val new: Boolean,
    @Column("CREATED_AT") val createdAt: Instant = Instant.now(),
)

enum class NotificationCategory {
    ANALYSIS,
    EVENT,
}
