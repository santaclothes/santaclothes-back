package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.Image
import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.event.ImageUploadCommand
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.support.FileSupports.Companion.resolvePath
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class ImageService(
    private val imageRepository: ImageRepository,
    private val publisher: ApplicationEventPublisher
) {
    fun findAllCareLabelsToProcess(): List<Image> {
        return imageRepository.findAllCareLabelsToProcess()
    }

    fun getNotClassifiedCareLabelImageByImageId(imageId: Long): Image {
        return imageRepository.findNotClassifiedCareLabelImageByImageId(imageId).orElseThrow()
    }

    fun getCareLabelImageByCareLabelId(careLabelId: Long): Image =
        imageRepository.findImageByCareLabelId(careLabelId).orElseThrow()

    fun getImagesByClothId(clothId: Long): List<Image> {
        return imageRepository.findByClothId(clothId)
    }

    fun getImageByFileName(fileName: String): Image = imageRepository.findByFileName(fileName).orElseThrow()

    fun upload(file: MultipartFile, type: ImageType, userToken: String, clothId: Long): String {
        val fileName = UUID.randomUUID().toString()
        val filePath = resolvePath("$fileName.png")
        val fileUrl = "$IMAGE_API_PREFIX$fileName"

        imageRepository.save(
            Image(
                fileName = fileName,
                filePath = filePath,
                fileUrl = fileUrl,
                type = type,
                clothId = clothId,
                userToken = userToken
            )
        )

        publisher.publishEvent(
            ImageUploadCommand(
                file = file,
                filePath = filePath,
            )
        )

        return fileName
    }

    fun save(image: Image): Image = imageRepository.save(image)

    fun saveAll(images: Iterable<Image>): Iterable<Image> = imageRepository.saveAll(images)

    companion object {
        private const val IMAGE_API_PREFIX = "/image/"
    }
}
