package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.config.CacheTemplate
import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.repository.ClothRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ClothService(
    @Autowired val clothRepository: ClothRepository,
    @Qualifier("clothCountCacheTemplate") val clothCountCacheTemplate: CacheTemplate<Long>,
) {
    fun incrementCount() {
        clothCountCacheTemplate[CACHE_NAME] = clothCountCacheTemplate[CACHE_NAME] ?: 0 + 1L
    }

    fun getCount() = clothCountCacheTemplate[CACHE_NAME] ?: 0

    fun addCareLabel(id: Long, careLabel: CareLabel): Cloth =
        clothRepository.findById(id).orElseThrow().apply {
            careLabel.clothId = id
            this.careLabel = careLabel
        }.run {
            clothRepository.save(this)
        }

    companion object {
        private const val CACHE_NAME = "cloth-count"
    }
}
