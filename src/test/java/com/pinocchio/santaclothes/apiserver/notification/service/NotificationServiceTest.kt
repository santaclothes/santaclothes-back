package com.pinocchio.santaclothes.apiserver.notification.service

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.notification.fixture.mockSendNotificationApi
import com.pinocchio.santaclothes.apiserver.notification.service.dto.FirebaseMessageWrapper
import com.pinocchio.santaclothes.apiserver.test.SpringTest
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
) : SpringTest() {
    @MockBean
    lateinit var tokenManager: TokenManager

    @Test
    fun sendTo() {
        val deviceToken = "token"
        val message = "test"
        val request = FirebaseMessageWrapper(deviceToken, "Santaclothes 알리미", message, "")

        mockSendNotificationApi(
            projectId = projectId,
            accessToken = accessToken,
            request = request,
            response = """
                            {
                                "name": "성공"     
                            }
                        """.trimIndent()
        )
        given { tokenManager.fcmAccessToken }.willReturn(accessToken)

        StepVerifier.create(sut.sendTo(deviceToken, message))
            .assertNext {
                then(it.name).isEqualTo("성공")
            }
            .verifyComplete()
    }
}
