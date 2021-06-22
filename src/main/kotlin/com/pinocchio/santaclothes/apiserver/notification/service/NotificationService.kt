package com.pinocchio.santaclothes.apiserver.notification.service

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import com.pinocchio.santaclothes.apiserver.entity.Notification
import com.pinocchio.santaclothes.apiserver.entity.NotificationCategory
import com.pinocchio.santaclothes.apiserver.notification.apiclient.NotificationSender
import com.pinocchio.santaclothes.apiserver.notification.repository.NotificationRepository
import com.pinocchio.santaclothes.apiserver.notification.service.dto.FirebaseMessageWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    @Autowired val webClient: NotificationSender,
    @Autowired val tokenManager: TokenManager,
    @Autowired val notificationRepository: NotificationRepository,
) {
    @Transactional
    fun sendTo(authorizationToken: AuthorizationToken, analysisRequest: AnalysisRequest) =
        FirebaseMessageWrapper(
            token = authorizationToken.deviceToken,
            title = MESSAGE_TITLE,
            body = "${analysisRequest.cloth.name}의 케어 라벨이 분석 완료됐습니다.",
            image = IMAGE_URL
        ).let {
            notificationRepository.save(
                Notification(
                    userToken = authorizationToken.userToken,
                    analysisRequestId = analysisRequest.id!!,
                    category = NotificationCategory.ANALYSIS,
                    new = true
                )
            )
            webClient.send(it, tokenManager.fcmAccessToken)
        }

    fun hasNew(userToken: String) =
        notificationRepository.findFirstByUserTokenAndNewOrderByCreatedAtDesc(userToken, true).isPresent

    fun findByUserToken(userToken: String) =
        notificationRepository.findByUserTokenAndCategory(userToken, NotificationCategory.ANALYSIS)

    fun view(id: Long): Notification =
        notificationRepository.save(notificationRepository.findById(id).orElseThrow().apply { this.new = false })

    companion object {
        private const val MESSAGE_TITLE = "Santaclothes 알리미"
        private const val IMAGE_URL = ""
    }
}
