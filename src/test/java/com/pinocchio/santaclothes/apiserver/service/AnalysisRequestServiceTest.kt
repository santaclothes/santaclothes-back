package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.Notification
import com.pinocchio.santaclothes.apiserver.entity.NotificationCategory
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.notification.repository.NotificationRepository
import com.pinocchio.santaclothes.apiserver.repository.AnalysisRequestRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile

class AnalysisRequestServiceTest(
    @Autowired val sut: AnalysisRequestService,
    @Autowired val userService: UserService
) : SpringDataTest() {
    @Test
    fun save() {
        // given
        val auth = userService.register("token", "name", AccountType.KAKAO).run {
            userService.login(token, "device")
        }
        val mockFile: MultipartFile = MockMultipartFile(
            "images",
            "fileName",
            "application/json",
            "test".toByteArray()
        )

        // when
        val saved = sut.save(
            auth.accessToken,
            AnalysisRequestDocument(
                mockFile,
                mockFile,
                "clothName",
                ClothesType.TOP,
                ClothesColor.BEIGE
            )
        )

        // then
        val expected = sut.getById(saved.id!!)
        with(expected) {
            then(this.id).isEqualTo(saved.id)
            then(this.userToken).isEqualTo(saved.userToken)
            then(this.status).isEqualTo(saved.status)
        }
    }

    @Test
    fun toSaved(@Autowired analysisRequestRepository: AnalysisRequestRepository) {
        // given
        val actual = analysisRequestRepository.save(
            AnalysisRequest(
                userToken = "userToken",
                status = AnalysisStatus.NOTIFIED,
                cloth = Cloth(name = "test", color = ClothesColor.RED, type = ClothesType.TOP, userToken = "userToken")
            )
        )

        // when
        val expected = sut.withStatus(actual.id!!, AnalysisStatus.DONE)

        then(expected.status).isEqualTo(AnalysisStatus.DONE)
    }

    @Test
    fun toSavedWithEvent(
        @Autowired analysisRequestRepository: AnalysisRequestRepository,
        @Autowired notificationRepository: NotificationRepository
    ) {
        // given 
        val analysisRequest = analysisRequestRepository.save(
            AnalysisRequest(
                userToken = "userToken",
                status = AnalysisStatus.NOTIFIED,
                cloth = Cloth(name = "test", color = ClothesColor.RED, type = ClothesType.TOP, userToken = "userToken")
            )
        )
        val analysisRequestId = analysisRequest.id!!
        notificationRepository.save(
            Notification(
                userToken = "userToken",
                analysisRequestId = analysisRequestId,
                clothName = "test",
                category = NotificationCategory.ANALYSIS,
                new = true
            )
        )

        // when
        sut.withStatus(analysisRequestId, AnalysisStatus.DONE)

        val expected = notificationRepository.findByAnalysisRequestId(analysisRequestId)
        then(expected).allMatch { !it.new }
    }

    @Test
    fun toDeletedWhenNotified(@Autowired analysisRequestRepository: AnalysisRequestRepository) {
        // given
        val actual = analysisRequestRepository.save(
            AnalysisRequest(
                userToken = "userToken",
                status = AnalysisStatus.NOTIFIED,
                cloth = Cloth(name = "test", color = ClothesColor.RED, type = ClothesType.TOP, userToken = "userToken")
            )
        )

        // when
        val expected = sut.withStatus(actual.id!!, AnalysisStatus.DELETED)

        then(expected.status).isEqualTo(AnalysisStatus.DELETED)
    }

    @Test
    fun toDeletedWhenDone(@Autowired analysisRequestRepository: AnalysisRequestRepository) {
        // given
        val actual = analysisRequestRepository.save(
            AnalysisRequest(
                userToken = "userToken",
                status = AnalysisStatus.DONE,
                cloth = Cloth(name = "test", color = ClothesColor.RED, type = ClothesType.TOP, userToken = "userToken")
            )
        )

        // when
        val expected = sut.withStatus(actual.id!!, AnalysisStatus.DELETED)

        then(expected.status).isEqualTo(AnalysisStatus.DELETED)
    }
}
