package com.pinocchio.santaclothes.apiserver.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebClientConfig {

    @Bean
    @ConfigurationProperties(prefix = "google-fcm")
    fun notificationApiProperties() = ApiProperties()

    @Bean
    fun notificationWebClient(
        webClientBuilder: WebClient.Builder,
        @Qualifier("notificationApiProperties") apiProperties: ApiProperties
    ): WebClient {
        return webClientBuilder
            .baseUrl(apiProperties.url!!)
            .defaultHeaders { headers -> headers.contentType = MediaType.APPLICATION_JSON }
            .build()
    }
}

data class ApiProperties(
    var url: String? = null,
    var connectionTimeoutMillis: Int = 2_000,
    var readTimeoutMillis: Int = 2_000
)
