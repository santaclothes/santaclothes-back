package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.type.*
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ClothRepositoryTest(@Autowired val sut: ClothRepository) : SpringDataTest() {
    @Test
    fun findById() {
        val expected = sut.save(Cloth(name = "티셔츠", color = ClothesColor.GREEN, type = ClothesType.TOP))

        val actual = sut.findById(expected.id!!).get()

        then(actual).isEqualTo(expected)
    }

    @Test
    fun save() {
        val cloth = Cloth(name = "티셔츠", color = ClothesColor.BEIGE, type = ClothesType.TOP)

        val saved = sut.save(cloth)

        val actual = sut.findById(saved.id!!).get()
        then(actual).isEqualTo(saved)
    }

    @Test
    fun saveWithClothLabels() {
        val saved = sut.save(
            Cloth(
                name = "티셔츠",
                color = ClothesColor.BEIGE,
                type = ClothesType.TOP,
                careLabel = CareLabel(
                    waterType = WaterType.FORBIDDEN_BLOWER_JP,
                    bleachType = BleachType.ALL_JP,
                    dryType = DryType.DRY_BLOWER_FORBIDDEN_KR,
                    dryCleaning = DryCleaning.DRY_CLEANING_FORBIDDEN_JP,
                    ironingType = IroningType.FORBIDDEN_JP
                )
            )
        )

        val actual = sut.findById(saved.id!!).get()

        then(actual.careLabel).isNotNull
    }
}
