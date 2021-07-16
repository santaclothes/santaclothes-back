package com.pinocchio.santaclothes.apiserver.notification.service

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import com.pinocchio.santaclothes.apiserver.entity.Notification
import com.pinocchio.santaclothes.apiserver.entity.NotificationCategory
import com.pinocchio.santaclothes.apiserver.notification.apiclient.NotificationSender
import com.pinocchio.santaclothes.apiserver.notification.repository.NotificationRepository
import com.pinocchio.santaclothes.apiserver.notification.service.dto.FirebaseMessageWrapper
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    private val webClient: NotificationSender,
    private val tokenManager: TokenManager,
    private val notificationRepository: NotificationRepository,
) {
    @Transactional
    fun sendTo(authorizationToken: AuthorizationToken, request: NotificationSendRequest) =
        FirebaseMessageWrapper(
            token = authorizationToken.deviceToken,
            title = MESSAGE_TITLE,
            body = "${request.clothName}의 케어 라벨이 분석 완료됐습니다.",
            image = IMAGE_URL
        ).let {
            notificationRepository.save(
                Notification(
                    userToken = authorizationToken.userToken,
                    clothName = request.clothName,
                    analysisRequestId = request.analysisRequestId,
                    category = NotificationCategory.ANALYSIS,
                    new = true
                )
            )
            webClient.send(it, tokenManager.fcmAccessToken)
        }

    fun hasNew(userToken: String) =
        notificationRepository.findFirstByUserTokenAndNewOrderByCreatedAtDesc(userToken, true).isPresent

    fun findNewByUserTokenWithPaging(userToken: String, page: Int = 0, size: Int = 20) =
        notificationRepository.findByUserTokenAndCategoryAndNew(
            userToken,
            NotificationCategory.ANALYSIS,
            true,
            PageRequest.of(page, size, Sort.by("id"))
        )

    fun viewByAnalysisRequestId(analysisRequestId: Long) {
        notificationRepository.saveAll(
            notificationRepository.findByAnalysisRequestId(analysisRequestId)
                .filter { it.new }
                .map {
                    it.new = false
                    it
                }.toList()
        )
    }

    companion object {
        private const val MESSAGE_TITLE = "Santaclothes 알리미"
        private const val IMAGE_URL = ""
    }
}

data class NotificationSendRequest(
    val clothName: String,
    val analysisRequestId: Long
)
