package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.Image
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ImageService(@Autowired val imageRepository: ImageRepository) {
    fun findLabels(): List<Image> {
        return imageRepository.findAll().toList()
    }

    fun findLabel(i: Int): Image {
        return imageRepository.findAll().toList()[i]
    }

}
