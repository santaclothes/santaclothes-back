package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.Image
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ImageService(@Autowired val imageRepository: ImageRepository) {
    fun findLabels(): List<Image> {
        // TODO: 라벨만 조회하도록 수정
        //       테스트 코드 작성
        return imageRepository.findAll().toList()
    }
}
