package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.config.CacheConfig
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.repository.AuthorizationTokenRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthorizationTokenService(private val authorizationTokenRepository: AuthorizationTokenRepository) {
    @Cacheable(cacheNames = [CacheConfig.AUTHORIZATION_TOKEN_BY_ACCESS_TOKEN_NAME], key = "#accessToken")
    fun getByAccessToken(accessToken: UUID): AuthorizationToken =
        authorizationTokenRepository.findFirstByAccessTokenOrderByCreatedAtDesc(accessToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN) }

    @Cacheable(cacheNames = [CacheConfig.AUTHORIZATION_TOKEN_BY_USER_TOKEN_NAME], key = "#userToken")
    fun getByUserToken(userToken: String): AuthorizationToken =
        authorizationTokenRepository.findFirstByUserTokenOrderByCreatedAtDesc(userToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.USER_TOKEN_NOT_EXISTS) }

    fun getByRefreshToken(refreshToken: UUID): AuthorizationToken =
        authorizationTokenRepository.findFirstByRefreshTokenOrderByCreatedAtDesc(refreshToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_REFRESH_TOKEN) }

    @Caching(
        evict = [
            CacheEvict(
                cacheNames = [CacheConfig.AUTHORIZATION_TOKEN_BY_ACCESS_TOKEN_NAME],
                key = "#authorizationToken.accessToken"
            ),
            CacheEvict(
                cacheNames = [CacheConfig.AUTHORIZATION_TOKEN_BY_USER_TOKEN_NAME],
                key = "#authorizationToken.userToken"
            )
        ]
    )
    fun save(authorizationToken: AuthorizationToken): AuthorizationToken =
        authorizationTokenRepository.save(authorizationToken)
}
