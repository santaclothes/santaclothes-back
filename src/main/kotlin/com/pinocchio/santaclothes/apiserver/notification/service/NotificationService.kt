package com.pinocchio.santaclothes.apiserver.notification.service

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.notification.apiclient.NotificationSender
import com.pinocchio.santaclothes.apiserver.notification.service.dto.FirebaseMessageWrapper
import com.pinocchio.santaclothes.apiserver.notification.service.dto.NotificationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NotificationService(
    @Autowired val webClient: NotificationSender,
    @Autowired val tokenManager: TokenManager
) {
    fun sendTo(deviceToken: String, message: String): Mono<NotificationResponse> = FirebaseMessageWrapper(
        token = deviceToken,
        title = MESSAGE_TITLE,
        body = message,
        image = IMAGE_URL
    ).let {
        webClient.send(it, tokenManager.fcmAccessToken)
    }

    companion object {
        private const val MESSAGE_TITLE = "Santaclothes 알리미"
        private const val IMAGE_URL = ""
    }
}
