package com.pinocchio.santaclothes.apiserver.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CacheConfig {
    @Bean
    fun clothCountCacheTemplate(cacheManager: CacheManager) =
        CacheTemplate<Long>(cacheManager.getCache(CLOTH_COUNT_NAME)!!)

    @Bean
    fun cacheManager(): CacheManager {
        val caches = listOf(
            CaffeineCache(
                AUTHORIZATION_TOKEN_BY_USER_TOKEN_NAME,
                Caffeine.newBuilder().recordStats() // TODO: caffeine 설정 검토
                    .maximumSize(1000L) // entries 개수
                    .build(),
            ),
            CaffeineCache(
                NOTICE_NAME,
                Caffeine.newBuilder().recordStats() // TODO: caffeine 설정 검토
                    .maximumSize(10L)
                    .build()
            ),
            CaffeineCache(
                CLOTH_COUNT_NAME,
                Caffeine.newBuilder().recordStats()
                    .maximumSize(0L)
                    .build()
            ),
            CaffeineCache(
                AUTHORIZATION_TOKEN_BY_ACCESS_TOKEN_NAME,
                Caffeine.newBuilder().recordStats()
                    .maximumSize(1000L)
                    .build()
            )
        )

        return SimpleCacheManager().apply { setCaches(caches) }
    }

    companion object {
        const val AUTHORIZATION_TOKEN_BY_USER_TOKEN_NAME = "AUTHORIZATION_TOKEN_BY_USER_TOKEN"
        const val NOTICE_NAME = "NOTICE"
        const val CLOTH_COUNT_NAME = "CLOTH_COUNT"
        const val AUTHORIZATION_TOKEN_BY_ACCESS_TOKEN_NAME = "AUTHORIZATION_TOKEN_BY_ACCESS_TOKEN"
    }
}

class CacheTemplate<T>(
    private val cache: Cache
) {
    @Suppress("UNCHECKED_CAST")
    operator fun get(key: Any): T? = cache[key]?.get() as? T

    operator fun set(key: Any, value: T) = cache.put(key, value)

    fun evict(key: Any) = cache.evict(key)
}
