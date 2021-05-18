package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.AnalysisRequestView
import com.pinocchio.santaclothes.apiserver.controller.dto.CareLabelDetail
import com.pinocchio.santaclothes.apiserver.controller.dto.HomeView
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ViewService(
    @Autowired val userService: UserService,
    @Autowired val noticeService: NoticeService,
    @Autowired val clothService: ClothService,
    @Autowired val analysisRequestService: AnalysisRequestService,
) {
    fun homeView(accessToken: UUID): HomeView {
        val user = userService.findByAccessToken(accessToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN) }
        val notices = noticeService.findAllNotices()
        val clothesCount = clothService.getCount()
        // TODO: notification 확인 여부
        return HomeView(user.name, clothesCount, notices, false)
    }

    fun analysisRequestView(accessToken: UUID, requestId: Long): AnalysisRequestView {
        val analysisRequest = analysisRequestService.getById(requestId)
        val cloth = analysisRequest.cloth
        val careLabelDetails: List<CareLabelDetail> = listOf()


        val howToTitle = "폴리는 물세탁이 가장 좋은 섬유입니다."
        val howToContent =
            "폴리 특성상 마찰에 의한 보풀이 잘 일어날 수 있는 섬유이기 때문에 손세탁으로 세탁하시면 좋습니다." +
                    " 세제는 가능하면 중성세제나 약산성 전용세제를 사용하시고, 세탁은 무조건 실온물을 사용하시면 안전합니다."

        val clothImageUrl = "test"
        // TODO: careLabel 조회
        val careLabelImageUrls: List<String> = listOf()
        return AnalysisRequestView(
            clothName = cloth.name,
            howToTitle = howToTitle,
            howToContent = howToContent,
            clothImageUrl = clothImageUrl,
            careLabelImageUrls = careLabelImageUrls,
            careLabelDetails = careLabelDetails,
            Instant.now()
        )
    }
}
