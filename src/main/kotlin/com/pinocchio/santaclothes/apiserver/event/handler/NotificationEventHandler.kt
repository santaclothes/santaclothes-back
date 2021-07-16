package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.event.AnalysisRequestNotifiedEvent
import com.pinocchio.santaclothes.apiserver.notification.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class NotificationEventHandler(@Autowired val notificationService: NotificationService) {
    @EventListener
    fun notificationViewed(event: AnalysisRequestNotifiedEvent) {
        notificationService.viewByAnalysisRequestId(event.analysisRequestId)
    }
}
