package com.pinocchio.santaclothes.apiserver.config

import com.pinocchio.santaclothes.apiserver.config.interceptor.SecurityInterceptor
import com.pinocchio.santaclothes.apiserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SecurityConfig(
    @Autowired val userService: UserService
) : WebMvcConfigurer {
    @Bean
    fun securityInterceptor(): SecurityInterceptor = SecurityInterceptor(userService)

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(securityInterceptor())
            .addPathPatterns("/api/**")
            .addPathPatterns("/view/**")
    }
}
