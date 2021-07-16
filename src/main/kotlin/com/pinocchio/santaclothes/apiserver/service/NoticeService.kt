package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.config.CacheConfig.Companion.NOTICE_NAME
import com.pinocchio.santaclothes.apiserver.entity.Notice
import com.pinocchio.santaclothes.apiserver.repository.NoticeRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class NoticeService(private val noticeRepository: NoticeRepository) {
    @Cacheable(cacheNames = [NOTICE_NAME])
    fun findAllNotices(): List<Notice> = noticeRepository.findAll().toList()
}
