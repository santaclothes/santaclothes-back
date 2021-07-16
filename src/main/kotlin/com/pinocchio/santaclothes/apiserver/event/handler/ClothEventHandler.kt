package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.event.AnalysisRequestDoneEvent
import com.pinocchio.santaclothes.apiserver.event.CareLabelUpdateCommand
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
    fun careLabelUpdateEvent(command: CareLabelUpdateCommand) {
        val careLabelImage =
            imageRepository.findNotClassifiedCareLabelImageByImageId(command.careLabelImageId).orElseThrow()
        careLabelImage.careLabelId = command.careLabelId
        imageRepository.save(careLabelImage)

        analysisRequestService.withStatus(command.analysisRequestId, AnalysisStatus.CLASSIFIED)
    }

    @EventListener
    fun analysisRequestDone(event: AnalysisRequestDoneEvent) {
        clothService.incrementCount()
    }
}
