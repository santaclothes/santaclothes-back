package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.event.ImageUploadEvent
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher

class ImageEventHandlerTest @Autowired constructor(
    private val publisher: ApplicationEventPublisher,
    private val imageRepository: ImageRepository,
) : SpringDataTest() {
    @Test
    fun uploadEvent() {
        // given
        val clothId = 1L
        val imageUploadEvent = ImageUploadEvent(
            fileName = "fileName",
            filePath = "filePath",
            fileUrl = "fileUrl",
            type = ImageType.CARE_LABEL,
            clothId = clothId,
            userToken = "userToken"
        )

        // when
        publisher.publishEvent(imageUploadEvent)

        // then
        val actual = imageRepository.findByClothId(clothId)[0]
        then(actual.fileName).isEqualTo(imageUploadEvent.fileName)
        then(actual.filePath).isEqualTo(imageUploadEvent.filePath)
        then(actual.fileUrl).isEqualTo(imageUploadEvent.fileUrl)
        then(actual.type).isEqualTo(imageUploadEvent.type)
        then(actual.clothId).isEqualTo(imageUploadEvent.clothId)
        then(actual.userToken).isEqualTo(imageUploadEvent.userToken)
    }
}
