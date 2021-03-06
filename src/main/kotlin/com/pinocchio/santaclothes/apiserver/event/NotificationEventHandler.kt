package com.pinocchio.santaclothes.apiserver.event

import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.notification.service.NotificationSendRequest
import com.pinocchio.santaclothes.apiserver.notification.service.NotificationService
import com.pinocchio.santaclothes.apiserver.service.AnalysisRequestService
import com.pinocchio.santaclothes.apiserver.service.AuthorizationTokenService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class NotificationEventHandler(
    private val notificationService: NotificationService,
    private val analysisRequestService: AnalysisRequestService,
    private val authorizationTokenService: AuthorizationTokenService,
) {
    @EventListener
    @Async
    fun sendNotification(command: NotificationSendCommand) {
        val analysisRequest = analysisRequestService.getById(command.analysisRequestId)
        val authorizationToken = authorizationTokenService.getByUserToken(analysisRequest.userToken)
        val analysisRequestId = analysisRequest.id!!

        notificationService.sendTo(
            authorizationToken,
            NotificationSendRequest(
                analysisRequest.cloth.name,
                analysisRequestId
            )
        ).block()

        analysisRequestService.withStatus(analysisRequestId, AnalysisStatus.NOTIFIED)
    }

    @EventListener
    fun notificationViewed(event: AnalysisRequestNotifiedEvent) {
        notificationService.viewByAnalysisRequestId(event.analysisRequestId)
    }
}
