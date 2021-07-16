package com.pinocchio.santaclothes.apiserver.notification.apiclient

import com.pinocchio.santaclothes.apiserver.notification.service.dto.FirebaseMessageWrapper
import com.pinocchio.santaclothes.apiserver.notification.service.dto.NotificationResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class NotificationSender(
    @Qualifier("notificationWebClient") val webClient: WebClient,
) {
    fun send(
        messageWrapper: FirebaseMessageWrapper,
        accessToken: String
    ): Mono<NotificationResponse> = webClient.post()
        .uri("/messages:send")
        .body(BodyInserters.fromValue(messageWrapper))
        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, ClientResponse::createException)
        .bodyToMono(NotificationResponse::class.java)
}
