package com.pinocchio.santaclothes.apiserver.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/img/**", "/css/**")
            .addResourceLocations(
                "classpath:/static/img/",
                "classpath:/static/css/",
                "classpath:/static/js/"
            )
    }
}
