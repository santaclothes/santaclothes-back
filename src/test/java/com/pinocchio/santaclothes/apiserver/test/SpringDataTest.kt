package com.pinocchio.santaclothes.apiserver.test

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.transaction.annotation.Transactional

@Transactional
abstract class SpringDataTest : SpringTest() {
    @AfterEach
    fun clearTest(@Autowired cacheManager: CacheManager) {
        cacheManager.cacheNames.map {
            cacheManager.getCache(it)!!.clear()
        }
    }
}
