package com.pinocchio.santaclothes.apiserver.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Table
data class UserToken(
    @JsonIgnore @Id var id: Long? = null,
    var userToken: String,
    val deviceToken: String,
    val accessToken: UUID = UUID.randomUUID(),
    val refreshToken: UUID = UUID.randomUUID(),
    val expiredAt: Instant = Instant.now().plus(7, ChronoUnit.DAYS),
    @JsonIgnore val createdAt: Instant = Instant.now()
) {
    fun isExpired(instant: Instant): Boolean {
        return !expiredAt.isAfter(instant)
    }
}
