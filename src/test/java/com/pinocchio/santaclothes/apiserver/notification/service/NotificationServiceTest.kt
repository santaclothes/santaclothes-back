package com.pinocchio.santaclothes.apiserver.notification.service

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import com.pinocchio.santaclothes.apiserver.entity.Cloth
import com.pinocchio.santaclothes.apiserver.entity.Notification
import com.pinocchio.santaclothes.apiserver.entity.NotificationCategory
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.fixture.mockSendNotificationApi
import com.pinocchio.santaclothes.apiserver.notification.repository.NotificationRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.test.StepVerifier

private const val accessToken = "accessToken"

class NotificationServiceTest(
    @Autowired val sut: NotificationService,
    @Value("\${google-fcm.project-id}") val projectId: String
) : SpringDataTest() {
    @MockBean
    lateinit var tokenManager: TokenManager

    @Test
    fun sendTo(@Autowired notificationRepository: NotificationRepository) {
        // given
        val userToken = "userToken"
        val deviceToken = "deviceToken"
        given { tokenManager.fcmAccessToken }.willReturn(accessToken)

        val authorizationToken = AuthorizationToken(userToken = userToken, deviceToken = deviceToken)
        val analysisRequest = AnalysisRequest(
            id = 1L,
            userToken = userToken,
            cloth = Cloth(
                name = "옷",
                userToken = userToken,
                color = ClothesColor.BEIGE,
                type = ClothesType.TOP
            )
        )

        mockSendNotificationApi(
            projectId = projectId,
            accessToken = accessToken,
            response = """
                            {
                                "name": "성공"     
                            }
            """.trimIndent()
        )

        // when
        StepVerifier.create(sut.sendTo(authorizationToken, analysisRequest))
            .assertNext { then(it.name).isEqualTo("성공") }
            .verifyComplete()

        val actualNotifications = notificationRepository.findByUserTokenAndCategoryAndNew(
            userToken,
            NotificationCategory.ANALYSIS,
            true
        )
        then(actualNotifications).isNotEmpty
    }

    @Test
    fun viewByAnalysisRequestId(@Autowired notificationRepository: NotificationRepository) {
        // given
        val analysisRequestId: Long = 1
        notificationRepository.saveAll(
            listOf(
                Notification(
                    userToken = "userToken",
                    clothName = "clothName",
                    analysisRequestId = analysisRequestId,
                    category = NotificationCategory.ANALYSIS
                ),
                Notification(
                    userToken = "userToken",
                    clothName = "clothName",
                    analysisRequestId = analysisRequestId,
                    category = NotificationCategory.ANALYSIS
                ),
                Notification(
                    userToken = "userToken",
                    clothName = "clothName",
                    analysisRequestId = analysisRequestId,
                    category = NotificationCategory.ANALYSIS
                )
            )
        )

        this.sut.viewByAnalysisRequestId(analysisRequestId)

        // then
        val actual = notificationRepository.findByAnalysisRequestId(analysisRequestId)
        then(actual).allMatch { !it.new }
    }
}
