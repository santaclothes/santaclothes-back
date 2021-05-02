package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.test.SpringTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ImageRepositoryTest(@Autowired val sut: ImageRepository) : SpringTest() {
    @Test
    fun findLabels() {
        then(sut.findById(1L)).isNotNull
    }
}
