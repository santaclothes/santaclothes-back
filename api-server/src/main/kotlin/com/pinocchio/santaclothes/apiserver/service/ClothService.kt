package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.repository.ClothRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClothService(@Autowired val clothRepository: ClothRepository) {
    fun save(cloth: Cloth): Cloth = clothRepository.save(cloth)

    fun getOne(id: Long): Cloth = clothRepository.findById(id).orElseThrow()
}
