package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface AuthorizationTokenRepository : CrudRepository<AuthorizationToken, Long> {
    fun findFirstByUserTokenOrderByCreatedAtDesc(userToken: String): Optional<AuthorizationToken>

    fun findFirstByRefreshTokenOrderByCreatedAtDesc(refreshToken: UUID): Optional<AuthorizationToken>

    fun findFirstByAccessTokenOrderByCreatedAtDesc(accessToken: UUID): Optional<AuthorizationToken>
}
