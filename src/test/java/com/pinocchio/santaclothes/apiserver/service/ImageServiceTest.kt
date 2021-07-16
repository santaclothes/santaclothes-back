package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.repository.ClothRepository
import com.pinocchio.santaclothes.apiserver.repository.ImageRepository
import com.pinocchio.santaclothes.apiserver.support.FileSupports.Companion.resolvePath
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile

class ImageServiceTest @Autowired constructor(
    private val sut: ImageService,
    private val clothRepository: ClothRepository,
    private val imageRepository: ImageRepository,
    private val userService: UserService
) : SpringDataTest() {
    @MockBean
    lateinit var eventPublisher: ApplicationEventPublisher

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
        val actual = imageRepository.findByClothId(clothId)[0]
        then(actual.fileName).isEqualTo(fileName)
        then(actual.filePath).isEqualTo(resolvePath("$fileName.png"))
        then(actual.type).isEqualTo(ImageType.CLOTH)
        then(actual.clothId).isEqualTo(clothId)
        then(actual.userToken).isEqualTo(userToken)
    }
}
