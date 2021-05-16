package com.pinocchio.santaclothes.apiserver.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.pinocchio.santaclothes.apiserver.entity.AuthorizationToken
import com.pinocchio.santaclothes.apiserver.entity.Notice
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
        CacheTemplate<AuthorizationToken>(cacheManager.getCache(Cache.AUTHORIZATION_TOKEN.name)!!)

    @Bean
    fun noticeCacheTemplate(cacheManager: CacheManager) =
        CacheTemplate<List<Notice>>(cacheManager.getCache(Cache.NOTICE.name)!!)

    @Bean
    fun clothCountCacheTemplate(cacheManager: CacheManager) =
        CacheTemplate<Long>(cacheManager.getCache(Cache.CLOTH_COUNT.name)!!)

    @Bean
    fun cacheUserTokenByAccessToken(cacheManager: CacheManager) =
        CacheTemplate<String>(cacheManager.getCache(Cache.ACCESS_TOKEN_TO_USER_TOKEN.name)!!)

    @Bean
    fun cacheManager(): CacheManager {
        val caches = listOf(
            CaffeineCache(
                Cache.AUTHORIZATION_TOKEN.name,
                Caffeine.newBuilder().recordStats() // TODO: caffeine 설정 검토
                    .maximumSize(1000L) // entries 개수
                    .build(),
            ),
            CaffeineCache(
                Cache.NOTICE.name,
                Caffeine.newBuilder().recordStats() // TODO: caffeine 설정 검토
                    .maximumSize(10L)
                    .build()
            ),
            CaffeineCache(
                Cache.CLOTH_COUNT.name,
                Caffeine.newBuilder().recordStats()
                    .maximumSize(0L)
                    .build()
            ),
            CaffeineCache(
                Cache.ACCESS_TOKEN_TO_USER_TOKEN.name,
                Caffeine.newBuilder().recordStats()
                    .maximumSize(1000L)
                    .build()
            )
        )

        return SimpleCacheManager().apply { setCaches(caches) }
    }

    enum class Cache(
        val cacheName : String
    ) {
        AUTHORIZATION_TOKEN("S"),
        NOTICE("A"),
        CLOTH_COUNT("s"),
        ACCESS_TOKEN_TO_USER_TOKEN("access");


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
