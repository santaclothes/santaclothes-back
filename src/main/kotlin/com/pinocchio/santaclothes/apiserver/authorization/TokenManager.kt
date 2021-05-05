package com.pinocchio.santaclothes.apiserver.authorization

import com.google.auth.oauth2.GoogleCredentials
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class TokenManager {
    val fcmAccessToken: String
        get() = GoogleCredentials // 내부적으로 캐싱 되있음
            .fromStream(ClassPathResource(FIREBASE_KEY_PATH).inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform")).also {
                it.refreshIfExpired()
            }.accessToken.tokenValue

    companion object {
        private const val FIREBASE_KEY_PATH = "firebase/santaclothes-key.json"
    }
}
