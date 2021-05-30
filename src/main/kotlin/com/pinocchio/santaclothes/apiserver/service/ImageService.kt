package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.Image
import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.event.ImageUploadEvent
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.support.FileSupports.Companion.createImageFolderIfNotExists
import com.pinocchio.santaclothes.apiserver.support.FileSupports.Companion.resolvePath
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID


@Service
class ImageService(
    @Autowired val imageRepository: ImageRepository,
    @Autowired val publisher: ApplicationEventPublisher
) {
    fun findAllCareLabelsToProcess(): List<Image> {
        return imageRepository.findAllCareLabelsToProcess()
    }

    fun getCareLabelById(id: Long): Image {
        return imageRepository.findCareLabelById(id).orElseThrow()
    }

    fun getImagesByClothId(clothId: Long): List<Image> {
        return imageRepository.findByClothId(clothId)
    }

    fun getImageByFileName(fileName: String): Image = imageRepository.findByFileName(fileName).orElseThrow()

    fun upload(file: MultipartFile, type: ImageType, userToken: String, clothId: Long): String {
        createImageFolderIfNotExists()

        val fileName = UUID.randomUUID().toString()
        val filePath = resolvePath("$fileName.png")
        val fileUrl = "$IMAGE_API_PREFIX$fileName"
        val transferFile = File(filePath)
        file.transferTo(transferFile)

        publisher.publishEvent(
            ImageUploadEvent(
                fileName = fileName,
                filePath = filePath,
                fileUrl = fileUrl,
                type = type,
                clothId = clothId,
                userToken = userToken
            )
        )

        return fileName
    }

    fun save(image: Image): Image = imageRepository.save(image)

    fun saveAll(images: Iterable<Image>): Iterable<Image> = imageRepository.saveAll(images)

    companion object {
        private const val IMAGE_API_PREFIX = "image"
    }
}
