package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.repository.ClothRepository
import com.pinocchio.santaclothes.apiserver.support.FileSupports.Companion.resolvePath
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile

import org.springframework.web.multipart.MultipartFile
import java.io.File


class ImageServiceTest(
    @Autowired val sut: ImageService,
    @Autowired val clothRepository: ClothRepository,
    @Autowired val userService: UserService
) : SpringDataTest() {

    @Test
    fun upload() {
        // given
        val userToken = "token"
        userService.register(userToken, "name", AccountType.KAKAO)
        val clothId = clothRepository.save(
            Cloth(
                name = "cloth1",
                color = ClothesColor.BEIGE,
                type = ClothesType.TOP,
                userToken = userToken
            )
        ).id!!
        val mockFile: MultipartFile = MockMultipartFile(
            "images",
            "fileName",
            "application/json",
            "test".toByteArray()
        )

        // when
        val fileName = this.sut.upload(mockFile, ImageType.CLOTH, userToken, clothId)

        // then
        val filePath = resolvePath("$fileName.png")
        val uploadFile = File(filePath)
        then(uploadFile).exists()

        val image = this.sut.getImagesByClothId(clothId)[0]
        then(image.type).isEqualTo(ImageType.CLOTH)
        then(image.clothId).isEqualTo(clothId)
        then(image.userToken).isEqualTo(userToken)
    }
}