package com.pinocchio.santaclothes.apiserver.notification.fixture

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.givenThat
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.pinocchio.santaclothes.apiserver.notification.service.dto.FirebaseMessageWrapper
import com.pinocchio.santaclothes.apiserver.support.JsonSupports
import org.springframework.http.HttpHeaders

fun mockSendNotificationApi(
    projectId: String,
    accessToken: String,
    request: FirebaseMessageWrapper,
    response: String
) {
    givenThat(
        post("/v1/projects/$projectId/messages:send")
            .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer $accessToken"))
            .withRequestBody(equalToObject(request))
            .willReturn(
                okJson(response)
            )
    )
}

private fun <T> equalToObject(obj: T) = WireMock.equalToJson(JsonSupports.toJsonString(obj))
