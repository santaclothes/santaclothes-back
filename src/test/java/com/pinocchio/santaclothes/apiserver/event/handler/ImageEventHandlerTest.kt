package com.pinocchio.santaclothes.apiserver.event.handler

import com.pinocchio.santaclothes.apiserver.event.ImageUploadCommand
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.File

class ImageEventHandlerTest @Autowired constructor(
    private val publisher: ApplicationEventPublisher,
) : SpringDataTest() {
    @Test
    fun uploadEvent() {
        // given
        val file: MultipartFile = MockMultipartFile(
            "images",
            "fileName",
            "application/json",
            "test".toByteArray()
        )
        val filePath = "filePath"
        val imageUploadEvent = ImageUploadCommand(
            file = file,
            filePath = filePath,
        )

        // when
        publisher.publishEvent(imageUploadEvent)

        // then
        val uploadFile = File(filePath)
        then(uploadFile).exists()
    }
}
