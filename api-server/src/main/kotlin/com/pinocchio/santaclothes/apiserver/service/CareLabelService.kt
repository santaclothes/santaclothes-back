package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.repository.CareLabelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CareLabelService(@Autowired val careLabelRepository: CareLabelRepository) {
    fun getOne(id: Long): CareLabel = careLabelRepository.findById(id).orElseThrow()
}
