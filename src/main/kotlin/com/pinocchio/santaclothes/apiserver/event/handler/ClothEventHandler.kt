package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.event.AnalysisRequestDoneEvent
import com.pinocchio.santaclothes.apiserver.event.CareLabelUpdateEvent
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.service.AnalysisRequestService
import com.pinocchio.santaclothes.apiserver.service.ClothService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ClothEventHandler(
    private val imageRepository: ImageRepository,
    private val analysisRequestService: AnalysisRequestService,
    private val clothService: ClothService,
) {
    @EventListener
    fun careLabelUpdateEvent(event: CareLabelUpdateEvent) {
        val careLabelImage =
            imageRepository.findNotClassifiedCareLabelImageByImageId(event.careLabelImageId).orElseThrow()
        careLabelImage.careLabelId = event.careLabelId
        imageRepository.save(careLabelImage)

        analysisRequestService.withStatus(event.analysisRequestId, AnalysisStatus.CLASSIFIED)
    }

    @EventListener
    fun analysisRequestDone(event: AnalysisRequestDoneEvent) {
        clothService.incrementCount()
    }
}
