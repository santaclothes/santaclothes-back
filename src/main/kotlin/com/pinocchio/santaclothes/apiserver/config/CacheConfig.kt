package com.pinocchio.santaclothes.apiserver.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.pinocchio.santaclothes.apiserver.entity.UserToken
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CacheConfig {
    @Bean
    fun userTokenCache() = CaffeineCache(
        "user-token",
        Caffeine.newBuilder().recordStats()
            .maximumSize(1000L) // entries 개수
            .build()
    )

    @Bean
    fun userTokenCacheTemplate(userTokenCache: Cache) = CacheTemplate<UserToken>(userTokenCache)

    @Bean
    fun cacheManager(): CacheManager = SimpleCacheManager().apply {
        setCaches(
            listOf(userTokenCache())
        )
    }
}

class CacheTemplate<T>(
    private val cache: Cache
) {
    @Suppress("UNCHECKED_CAST")
    operator fun get(key: String): T? = cache[key]?.get() as? T

    operator fun set(key: String, value: T) = cache.put(key, value)
}
