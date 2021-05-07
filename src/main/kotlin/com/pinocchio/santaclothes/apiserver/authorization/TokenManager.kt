package com.pinocchio.santaclothes.apiserver.authorization

import com.google.auth.oauth2.GoogleCredentials
import com.pinocchio.santaclothes.apiserver.config.CacheTemplate
import com.pinocchio.santaclothes.apiserver.entity.UserToken
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.repository.UserTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Component
class TokenManager(
    @Autowired private val userTokenRepository: UserTokenRepository,
    @Autowired private val cacheTemplate: CacheTemplate<UserToken>,
) {
    val fcmAccessToken: String
        get() = GoogleCredentials // 내부적으로 캐싱 되있음
            .fromStream(ClassPathResource(FIREBASE_KEY_PATH).inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform")).also {
                it.refreshIfExpired()
            }.accessToken.tokenValue

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun refreshAccessToken(refreshToken: UUID): UserToken =
        userTokenRepository.findFirstByRefreshTokenOrderByCreatedAtDesc(refreshToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_REFRESH_TOKEN) }
            .run {
                userTokenRepository.saveWithCache(
                    UserToken(
                        userToken = this.userToken,
                        deviceToken = this.deviceToken,
                        refreshToken = refreshToken
                    )
                )
            }

    fun validateAccessToken(accessToken: UUID) {
        val authorization = userTokenRepository.findFirstByAccessTokenOrderByCreatedAtDesc(accessToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN) }

        if (authorization.isExpiredWhen(Instant.now())) {
            throw TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN)
        }
    }

    fun acquireAccessToken(userToken: String, deviceToken: String): UserToken = runCatching {
        findUserToken(userToken)
    }.onSuccess {
        if (it.isExpiredWhen(Instant.now())) {
            throw TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN)
        }

        if (it.deviceToken != deviceToken) {
            userTokenRepository.saveWithCache(it.copy(deviceToken = deviceToken))
        }
    }.getOrElse { userTokenRepository.saveWithCache(UserToken(userToken = userToken, deviceToken = deviceToken)) }

    private fun findUserToken(userToken: String): UserToken =
        cacheTemplate[userToken] ?: userTokenRepository.findFirstByUserTokenOrderByCreatedAtDesc(userToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.USER_TOKEN_NOT_EXISTS) }

    companion object {
        private const val FIREBASE_KEY_PATH = "firebase/santaclothes-key.json"
    }

    fun UserTokenRepository.saveWithCache(entity: UserToken): UserToken = this.save(entity).apply {
        cacheTemplate[userToken] = this
    }
}
