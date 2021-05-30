package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.event.AddCareLabelEvent
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ClothEventHandler(
    @Autowired val imageRepository: ImageRepository
) {

    // @TransactionalEventListener? commit 된 후에 동작, 테스트에서는 Rollback이 이루어지기 떄문에 동작 X
    @EventListener
    fun addCareLabel(careLabelEvent: AddCareLabelEvent) {
        val careLabelImage = imageRepository.findCareLabelById(careLabelEvent.careLabelImageId).orElseThrow()
        careLabelImage.careLabelId = careLabelEvent.careLabelId
        imageRepository.save(careLabelImage)
    }
}