package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.config.CacheConfig
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface UserTokenRepository : CrudRepository<AuthorizationToken, Long> {
    fun findFirstByUserTokenOrderByCreatedAtDesc(userToken: String): Optional<AuthorizationToken>

    fun findFirstByRefreshTokenOrderByCreatedAtDesc(refreshToken: UUID): Optional<AuthorizationToken>

    fun findFirstByAccessTokenOrderByCreatedAtDesc(accessToken: UUID): Optional<AuthorizationToken>
}
