package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.*
import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.notification.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

private const val STRING_ABBREVIATE_LENGTH = 20

@Service
class ViewService(
    @Autowired val userService: UserService,
    @Autowired val noticeService: NoticeService,
    @Autowired val clothService: ClothService,
    @Autowired val analysisRequestService: AnalysisRequestService,
    @Autowired val imageService: ImageService,
    @Autowired val notificationService: NotificationService,
) {
    fun homeView(accessToken: UUID): HomeView {
        val user = userService.findByAccessToken(accessToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN) }
        val notices = noticeService.findAllNotices().map {
            it.copy(content = it.content.take(STRING_ABBREVIATE_LENGTH))
        }
        val clothesCount = clothService.getCount()
        val hasNewNotification = notificationService.hasNew(user.token)
        return HomeView(user.name, clothesCount, notices, hasNewNotification)
    }

    fun analysisRequestView(accessToken: UUID, requestId: Long): AnalysisRequestView {
        val user = userService.findByAccessToken(accessToken).orElseThrow()
        val analysisRequest = analysisRequestService.getById(requestId)
        val cloth = analysisRequest.cloth
        if (analysisRequest.status == AnalysisStatus.DONE) {
            throw NoSuchElementException("$requestId is not exists")
        }

        val howToTitle = "폴리는 물세탁이 가장 좋은 섬유입니다."
        val howToContent =
            "폴리 특성상 마찰에 의한 보풀이 잘 일어날 수 있는 섬유이기 때문에 손세탁으로 세탁하시면 좋습니다." +
                " 세제는 가능하면 중성세제나 약산성 전용세제를 사용하시고, 세탁은 무조건 실온물을 사용하시면 안전합니다."

        val images = imageService.getImagesByClothId(cloth.id!!)
        val clothImageUrl = images.first { it.type == ImageType.CLOTH }.fileUrl

        val careLabelImageUrls: List<String> = images
            .filter { it.type == ImageType.CARE_LABEL }
            .map { it.fileUrl }

        val careLabel = analysisRequest.cloth.careLabel!!
        val careLabelDetails = careLabel.toDetail()

        return AnalysisRequestView(
            userName = user.name,
            clothName = cloth.name,
            howToTitle = howToTitle,
            howToContent = howToContent,
            clothImageUrl = clothImageUrl,
            careLabelImageUrls = careLabelImageUrls,
            careLabelDetails = careLabelDetails,
            Instant.now()
        )
    }

    fun myPageView(accessToken: UUID): MyPageView = userService.findByAccessToken(accessToken).orElseThrow()
        .run {
            val myPageClothes = analysisRequestService.getByUserToken(token)
                .filter { it.status == AnalysisStatus.DONE }
                .map {
                    MyPageCloth(
                        clothId = it.cloth.id!!,
                        clothType = it.cloth.type,
                        imageUrl = imageService.getImagesByClothId(it.cloth.id!!)
                            .first { image -> image.type == ImageType.CLOTH }.fileUrl,
                        careLabelCount = if (it.cloth.careLabel == null) 0 else 1,
                        requestAt = it.createdAt
                    )
                }
                .toList()

            val myClothesCount = myPageClothes.count()

            MyPageView(
                userName = name,
                myClothesCount = myClothesCount.toLong(),
                myPageClothes = myPageClothes
            )
        }

    fun reportView(requestId: Long): ReportView {
        val analysisRequest = analysisRequestService.getById(requestId)
        if (analysisRequest.status != AnalysisStatus.NOTIFIED) {
            throw IllegalArgumentException("Report does not exist for given request id : $requestId")
        }
        val careLabel = analysisRequest.cloth.careLabel!!

        return ReportView(
            careLabelImageUrl = imageService.getCareLabelImageByCareLabelId(careLabel.id!!).fileUrl,
            careLabelDetails = careLabel.toDetail()
        )
    }

    fun CareLabel.toDetail(): List<CareLabelDetail> {
        return listOf(
            CareLabelDetail(
                waterType.imageUrl,
                waterType.name,
                waterType.description
            ),
            CareLabelDetail(
                dryType.imageUrl,
                dryType.name,
                dryType.description
            ),
            CareLabelDetail(
                dryCleaning.imageUrl,
                dryCleaning.name,
                dryCleaning.description
            ),
            CareLabelDetail(
                bleachType.imageUrl,
                bleachType.name,
                bleachType.description
            ),
            CareLabelDetail(
                ironingType.imageUrl,
                ironingType.name,
                ironingType.description
            )
        )
    }
}
