package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ClothServiceTest(
    @Autowired val sut: ClothService,
) : SpringDataTest() {
    @Test
    fun incrementCount() {
        sut.incrementCount()

        then(sut.getCount()).isEqualTo(1L)
    }
}
