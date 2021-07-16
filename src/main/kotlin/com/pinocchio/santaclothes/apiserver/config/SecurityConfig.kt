package com.pinocchio.santaclothes.apiserver.config

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.config.interceptor.SecurityInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SecurityConfig(private val tokenManager: TokenManager) : WebMvcConfigurer {
    @Bean
    fun securityInterceptor(): SecurityInterceptor = SecurityInterceptor(tokenManager)

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(securityInterceptor())
            .addPathPatterns("/api/**")
            .addPathPatterns("/view/**")
    }
}
