package com.pinocchio.santaclothes.apiserver.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.pinocchio.santaclothes.apiserver.support.JsonSupports
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {
    @Bean
    fun objectMapper(): ObjectMapper = JsonSupports.JSON_MAPPER
}
