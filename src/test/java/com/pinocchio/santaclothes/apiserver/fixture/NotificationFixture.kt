package com.pinocchio.santaclothes.apiserver.fixture

import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import org.springframework.http.HttpHeaders

fun mockSendNotificationApi(
    projectId: String,
    fcmToken: String,
    response: String =
        """
                            {
                                "name": "성공"
                            }
        """.trimIndent()
) {
    givenThat(
        post("/v1/projects/$projectId/messages:send")
            .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer $fcmToken"))
            .willReturn(
                okJson(response)
            )
    )
}
