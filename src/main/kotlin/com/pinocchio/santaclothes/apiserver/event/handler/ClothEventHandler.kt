package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.event.AnalysisDoneEvent
import com.pinocchio.santaclothes.apiserver.notification.service.NotificationService
import com.pinocchio.santaclothes.apiserver.repository.AnalysisRequestRepository
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.service.AuthorizationTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ClothEventHandler(
    @Autowired val imageRepository: ImageRepository,
    @Autowired val analysisRequestRepository: AnalysisRequestRepository,
    @Autowired val notificationService: NotificationService,
    @Autowired val authorizationTokenService: AuthorizationTokenService,
) {

    // @TransactionalEventListener? commit 된 후에 동작, 테스트에서는 Rollback이 이루어지기 떄문에 동작 X
    @EventListener
    fun analysisDone(careLabelEvent: AnalysisDoneEvent) {
        val careLabelImage = imageRepository.findCareLabelImageById(careLabelEvent.careLabelImageId).orElseThrow()
        careLabelImage.careLabelId = careLabelEvent.careLabelId
        imageRepository.save(careLabelImage)

        val analysisRequest = analysisRequestRepository.findByClothId(careLabelEvent.clothId).orElseThrow()
        analysisRequest.status = AnalysisStatus.CLASSIFIED
        analysisRequestRepository.save(analysisRequest)

        val authorizationToken = authorizationTokenService.getByUserToken(analysisRequest.userToken)
        notificationService.sendTo(
            authorizationToken,
            analysisRequest
        ).block()

        analysisRequest.status = AnalysisStatus.NOTIFIED
        analysisRequestRepository.save(analysisRequest)
    }
}
