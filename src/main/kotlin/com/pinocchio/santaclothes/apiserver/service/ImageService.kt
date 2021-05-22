package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.Image
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ImageService(@Autowired val imageRepository: ImageRepository) {
    fun findAllCareLabelsToProcess(): List<Image> {
        return imageRepository.findAllCareLabelsToProcess()
    }

    fun getCareLabelById(id: Long): Image {
        return imageRepository.findCareLabelById(id).orElseThrow()
    }

    fun save(image: Image): Image = imageRepository.save(image)

    fun saveAll(images: Iterable<Image>): Iterable<Image> = imageRepository.saveAll(images)
}
