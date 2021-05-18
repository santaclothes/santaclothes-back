package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.CareLabelIcon
import com.pinocchio.santaclothes.apiserver.controller.dto.toCareLabel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class AnalysisService(
    @Autowired val clothService: ClothService,
    @Autowired val imageService: ImageService,
) {
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun analysis(careLabelImageId: Long, careLabelIcon: CareLabelIcon) {
        val image = imageService.getCareLabelById(careLabelImageId)
        val savedCloth = clothService.addCareLabel(
            image.clothId!!, careLabelIcon.toCareLabel()
        )
        image.careLabelId = savedCloth.careLabel!!.id
        imageService.save(image)
    }
}
