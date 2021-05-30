package com.pinocchio.santaclothes.apiserver.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Table("AUTHORIZATION_TOKEN")
data class AuthorizationToken(
    @JsonIgnore @Id @Column("ID") var id: Long? = null,
    @JsonIgnore @Column("USER_TOKEN") var userToken: String,
    @JsonIgnore @Column("DEVICE_TOKEN") val deviceToken: String,
    @Column("ACCESS_TOKEN") val accessToken: UUID = UUID.randomUUID(),
    @Column("REFRESH_TOKEN") val refreshToken: UUID = UUID.randomUUID(),
    @Column("EXPIRED_AT") val expiredAt: Instant = Instant.now().plus(7, ChronoUnit.DAYS),
    @JsonIgnore @Column("CREATED_AT") val createdAt: Instant = Instant.now()
) {
    fun isExpiredWhen(instant: Instant): Boolean {
        return !expiredAt.isAfter(instant)
    }
}
