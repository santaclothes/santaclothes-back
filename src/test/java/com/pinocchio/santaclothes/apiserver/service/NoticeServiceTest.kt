package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.Notice
import com.pinocchio.santaclothes.apiserver.repository.NoticeRepository
import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class NoticeServiceTest(
    @Autowired val sut: NoticeService,
    @Autowired val noticeRepository: NoticeRepository,
) : SpringDataTest() {
    @Test
    fun findAllNotices() {
        val expected = listOf(
            Notice(title = "title1", hint = "hint1", content = "content1"),
            Notice(title = "title2", hint = "hint2", content = "content2"),
            Notice(title = "title3", hint = "hint3", content = "content3")
        )
        noticeRepository.saveAll(expected)

        val actual = sut.findAllNotices()

        then(actual).isEqualTo(expected)
    }

    @Test
    fun findAllNoticesCache() {
        val expected = listOf(
            Notice(title = "title1", hint = "hint1", content = "content1"),
            Notice(title = "title2", hint = "hint2", content = "content2"),
            Notice(title = "title3", hint = "hint3", content = "content3")
        )
        noticeRepository.saveAll(expected)
        sut.findAllNotices()
        noticeRepository.save(Notice(title = "title4", hint = "hint4", content = "content4"))

        val actual = sut.findAllNotices()

        then(actual.size).isEqualTo(3)
    }
}
