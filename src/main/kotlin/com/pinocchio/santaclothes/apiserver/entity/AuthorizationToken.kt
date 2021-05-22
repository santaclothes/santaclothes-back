package com.pinocchio.santaclothes.apiserver.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Table
data class AuthorizationToken(
    @JsonIgnore @Id @Column("id") var id: Long? = null,
    @JsonIgnore var userToken: String,
    @JsonIgnore val deviceToken: String,
    val accessToken: UUID = UUID.randomUUID(),
    val refreshToken: UUID = UUID.randomUUID(),
    val expiredAt: Instant = Instant.now().plus(7, ChronoUnit.DAYS),
    @JsonIgnore val createdAt: Instant = Instant.now()
) {
    fun isExpiredWhen(instant: Instant): Boolean {
        return !expiredAt.isAfter(instant)
    }
}
