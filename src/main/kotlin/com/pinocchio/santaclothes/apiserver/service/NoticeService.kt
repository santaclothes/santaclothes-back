package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.config.CacheTemplate
import com.pinocchio.santaclothes.apiserver.entity.Notice
import com.pinocchio.santaclothes.apiserver.repository.NoticeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NoticeService(
    @Autowired val noticeRepository: NoticeRepository,
    @Autowired val cacheTemplate: CacheTemplate<List<Notice>>
) {
    fun findAllNotices(): List<Notice> {
        if (cacheTemplate[CACHE_NAME] == null) {
            cacheTemplate[CACHE_NAME] = noticeRepository.findAll().toList()
        }
        return cacheTemplate[CACHE_NAME]!!
    }

    companion object {
        private const val CACHE_NAME = "notice"
    }
}
