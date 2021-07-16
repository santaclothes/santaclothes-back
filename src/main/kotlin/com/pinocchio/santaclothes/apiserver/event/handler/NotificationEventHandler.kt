package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.event.AnalysisRequestNotifiedEvent
import com.pinocchio.santaclothes.apiserver.event.NotificationSendEvent
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
    fun sendNotification(event: NotificationSendEvent) {
        val analysisRequest = analysisRequestService.getById(event.analysisRequestId)
        val authorizationToken = authorizationTokenService.getByUserToken(analysisRequest.userToken)
        notificationService.sendTo(
            authorizationToken,
            NotificationSendRequest(analysisRequest.cloth.name, analysisRequest.id!!)
        ).block()

        analysisRequestService.withStatus(analysisRequest.id!!, AnalysisStatus.NOTIFIED)
    }

    @EventListener
    fun notificationViewed(event: AnalysisRequestNotifiedEvent) {
        notificationService.viewByAnalysisRequestId(event.analysisRequestId)
    }
}
