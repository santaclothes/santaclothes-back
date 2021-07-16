package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.CareLabelIcon
import com.pinocchio.santaclothes.apiserver.controller.dto.toCareLabel
import com.pinocchio.santaclothes.apiserver.event.CareLabelUpdateCommand
import com.pinocchio.santaclothes.apiserver.event.NotificationSendCommand
import com.pinocchio.santaclothes.apiserver.repository.AnalysisRequestRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class AnalysisService(
    private val clothService: ClothService,
    private val imageService: ImageService,
    private val analysisRequestRepository: AnalysisRequestRepository,
    private val publisher: ApplicationEventPublisher,
) {
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun analysis(careLabelImageId: Long, careLabelIcon: CareLabelIcon) {
        val image = imageService.getNotClassifiedCareLabelImageByImageId(careLabelImageId)
        val clothId = image.clothId!!
        val saved = clothService.addCareLabel(clothId, careLabelIcon.toCareLabel())
        val analysisRequestId = analysisRequestRepository.findByClothId(clothId).orElseThrow().id!!

        publisher.publishEvent(
            CareLabelUpdateCommand(
                analysisRequestId = analysisRequestId,
                careLabelId = saved.careLabel!!.id!!,
                careLabelImageId = careLabelImageId,
            )
        )

        publisher.publishEvent(NotificationSendCommand(analysisRequestId = analysisRequestId))
    }
}
