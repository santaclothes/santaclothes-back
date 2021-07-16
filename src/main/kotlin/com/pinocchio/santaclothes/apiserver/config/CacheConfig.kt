package com.pinocchio.santaclothes.apiserver.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.pinocchio.santaclothes.apiserver.repository.NoticeRepository
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class CacheConfig {
    @Bean
    fun clothCountCacheTemplate(cacheManager: CacheManager) =
        CacheTemplate<Long>(cacheManager.getCache(CLOTH_COUNT_NAME)!!)

    @Bean
    fun cacheManager(noticeRepository: NoticeRepository): CacheManager {
        val caches = listOf(
            CaffeineCache(
                AUTHORIZATION_TOKEN_BY_USER_TOKEN_NAME,
                Caffeine.newBuilder().recordStats()
                    .maximumSize(1000L)
                    .expireAfterAccess(Duration.ofMinutes(5))
                    .build(),
            ),
            CaffeineCache(
                NOTICE_NAME,
                Caffeine.newBuilder().recordStats()
                    .maximumSize(10L)
                    .refreshAfterWrite(Duration.ofMinutes(10))
                    .build { noticeRepository.findAll() }
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
                    .expireAfterAccess(Duration.ofMinutes(5))
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
