package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.CareLabelIcon
import com.pinocchio.santaclothes.apiserver.controller.dto.toCareLabel
import com.pinocchio.santaclothes.apiserver.event.AnalysisDoneEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class AnalysisService(
    @Autowired val clothService: ClothService,
    @Autowired val imageService: ImageService,
    @Autowired val publisher: ApplicationEventPublisher,
) {
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun analysis(careLabelImageId: Long, careLabelIcon: CareLabelIcon) {
        val image = imageService.getCareLabelImageById(careLabelImageId)
        val clothId = image.clothId!!
        val saved = clothService.addCareLabel(clothId, careLabelIcon.toCareLabel())
        publisher.publishEvent(
            AnalysisDoneEvent(
                clothId = clothId,
                careLabelId = saved.careLabel!!.id!!,
                careLabelImageId = careLabelImageId,
            )
        )
    }
}
