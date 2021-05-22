package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File

class AnalysisRequestServiceTest(
    @Autowired val sut: AnalysisRequestService,
    @Autowired val userService: UserService
) : SpringDataTest() {
    @Test
    fun save() {
        val auth = userService.register("token", "name", AccountType.KAKAO).run {
            userService.login(token, "device")
        }
        val mockFile = File("mock")

        val saved = sut.save(
            AnalysisRequestDocument(
                auth.accessToken,
                mockFile,
                mockFile,
                "clothName",
                ClothesType.TOP,
                ClothesColor.BEIGE
            )
        )

        val request = sut.getById(saved.id!!)
        then(request).isEqualTo(saved)
    }
}