package com.pinocchio.santaclothes.apiserver.notification.apiclient

import com.pinocchio.santaclothes.apiserver.fixture.mockSendNotificationApi
import com.pinocchio.santaclothes.apiserver.notification.service.dto.FirebaseMessageWrapper
import com.pinocchio.santaclothes.apiserver.test.SpringTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import reactor.test.StepVerifier

class NotificationSenderTest(
    @Autowired val sut: NotificationSender,
    @Value("\${google-fcm.project-id}") val projectId: String
) : SpringTest() {

    @Test
    fun send() {
        val accessToken = "token"
        val message = "테스트"
        val deviceToken = "deviceToken"
        val request = FirebaseMessageWrapper(deviceToken, "Santaclothes 알리미", message, "")

        mockSendNotificationApi(
            projectId = projectId,
            accessToken = accessToken,
            response = """
                            {
                                "name": "성공"
                            }
            """.trimIndent()
        )

        StepVerifier.create(
            sut.send(
                request,
                accessToken = accessToken
            )
        )
            .assertNext {
                then(it.name).isEqualTo("성공")
            }
            .verifyComplete()
    }
}
