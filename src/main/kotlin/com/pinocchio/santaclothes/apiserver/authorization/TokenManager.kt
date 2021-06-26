package com.pinocchio.santaclothes.apiserver.authorization

import com.google.auth.oauth2.GoogleCredentials
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.service.AuthorizationTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Component
class TokenManager(
    @Autowired private val authorizationTokenService: AuthorizationTokenService,
) {
    val fcmAccessToken: String
        get() = GoogleCredentials // 내부적으로 캐싱 되있음
            .fromStream(ClassPathResource(FIREBASE_KEY_PATH).inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform")).also {
                it.refreshIfExpired()
            }.accessToken.tokenValue

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun refreshAccessToken(refreshToken: UUID): AuthorizationToken =
        authorizationTokenService.getByRefreshToken(refreshToken)
            .run {
                authorizationTokenService.save(
                    AuthorizationToken(
                        userToken = this.userToken,
                        deviceToken = this.deviceToken,
                        refreshToken = refreshToken
                    )
                )
            }

    fun validateAccessToken(accessToken: UUID) {
        val authorization = authorizationTokenService.getByAccessToken(accessToken)

        if (authorization.isExpiredWhen(Instant.now())) {
            throw TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN)
        }
    }

    fun acquireAccessToken(userToken: String, deviceToken: String): AuthorizationToken = runCatching {
        authorizationTokenService.getByUserToken(userToken)
    }.onSuccess {
        if (it.isExpiredWhen(Instant.now())) {
            refreshAccessToken(it.refreshToken)
        }

        if (it.deviceToken != deviceToken) {
            authorizationTokenService.save(it.copy(deviceToken = deviceToken))
        }
    }.getOrElse {
        authorizationTokenService.save(
            AuthorizationToken(
                userToken = userToken,
                deviceToken = deviceToken
            )
        )
    }

    companion object {
        private const val FIREBASE_KEY_PATH = "firebase/santaclothes-key.json"
    }
}
