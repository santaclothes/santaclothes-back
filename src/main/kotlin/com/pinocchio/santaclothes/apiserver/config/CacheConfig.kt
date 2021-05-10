package com.pinocchio.santaclothes.apiserver.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.pinocchio.santaclothes.apiserver.entity.Notice
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
    fun userTokenCacheTemplate(cacheManager: CacheManager) =
        CacheTemplate<UserToken>(cacheManager.getCache(Cache.USER_TOKEN.name)!!)

    @Bean
    fun noticeCacheTemplate(cacheManager: CacheManager) =
        CacheTemplate<List<Notice>>(cacheManager.getCache(Cache.NOTICE.name)!!)

    @Bean
    fun clothCountCacheTemplate(cacheManager: CacheManager) =
        CacheTemplate<Long>(cacheManager.getCache(Cache.CLOTH_COUNT.name)!!)

    @Bean
    fun cacheManager(): CacheManager {
        val userTokenCache = CaffeineCache(
            Cache.USER_TOKEN.name,
            Caffeine.newBuilder().recordStats() // TODO: caffeine 설정 검토
                .maximumSize(1000L) // entries 개수
                .build()
        )

        val noticeTokenCache = CaffeineCache(
            Cache.NOTICE.name,
            Caffeine.newBuilder().recordStats() // TODO: caffeine 설정 검토
                .maximumSize(10L)
                .build()
        )

        val clothCountCache = CaffeineCache(
            Cache.CLOTH_COUNT.name,
            Caffeine.newBuilder().recordStats()
                .maximumSize(0L)
                .build()
        )

        return SimpleCacheManager().apply {
            setCaches(
                listOf(
                    userTokenCache,
                    noticeTokenCache,
                    clothCountCache
                )
            )
        }
    }

    enum class Cache {
        USER_TOKEN,
        NOTICE,
        CLOTH_COUNT,
    }
}

class CacheTemplate<T>(
    private val cache: Cache
) {
    @Suppress("UNCHECKED_CAST")
    operator fun get(key: String): T? = cache[key]?.get() as? T

    operator fun set(key: String, value: T) = cache.put(key, value)

    operator fun set(key: String, value: List<T>) = cache.put(key, value)
}
