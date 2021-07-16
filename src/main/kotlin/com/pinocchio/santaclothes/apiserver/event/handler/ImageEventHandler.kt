package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.entity.Image
import com.pinocchio.santaclothes.apiserver.event.ImageUploadEvent
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ImageEventHandler(private val imageRepository: ImageRepository) {
    @EventListener
    fun uploadEvent(imageUploadEvent: ImageUploadEvent) {
        imageRepository.save(
            Image(
                fileName = imageUploadEvent.fileName,
                filePath = imageUploadEvent.filePath,
                fileUrl = imageUploadEvent.fileUrl,
                type = imageUploadEvent.type,
                clothId = imageUploadEvent.clothId,
                userToken = imageUploadEvent.userToken
            )
        )
    }
}
