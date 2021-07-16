package com.pinocchio.santaclothes.apiserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .securityContexts(listOf(securityContext()))
        .securitySchemes(listOf(apiKey()))
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.ant("/**"))
        .build()
        .useDefaultResponseMessages(false)
}

fun securityContext(): SecurityContext = SecurityContext.builder().securityReferences(defaultAuth()).build()

fun defaultAuth(): List<SecurityReference> {
    val authorizationScope = AuthorizationScope("global", "accessEverything")
    val authorizationScopes = arrayOf(
        authorizationScope
    )
    return listOf(SecurityReference("Token", authorizationScopes))
}

fun apiKey(): ApiKey = ApiKey("Token", "Authorization", "header")
