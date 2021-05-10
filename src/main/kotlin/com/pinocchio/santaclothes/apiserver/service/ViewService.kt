package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.HomeView
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ViewService(
    @Autowired val userService: UserService,
    @Autowired val noticeService: NoticeService,
    @Autowired val clothService: ClothService,
) {
    fun homeView(accessToken: UUID): HomeView {
        val user = userService.findByAccessToken(accessToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN) }
        val notices = noticeService.findAllNotices()
        val clothesCount = clothService.getCount()
        // TODO: notification 확인 여부
        return HomeView(user.name, clothesCount, notices, false)
    }
}
