package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.controller.dto.AnalysisRequestView
import com.pinocchio.santaclothes.apiserver.controller.dto.CareLabelDetail
import com.pinocchio.santaclothes.apiserver.controller.dto.HomeView
import com.pinocchio.santaclothes.apiserver.controller.dto.MyPageCloth
import com.pinocchio.santaclothes.apiserver.controller.dto.MyPageView
import com.pinocchio.santaclothes.apiserver.controller.dto.ReportView
import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.entity.ImageType
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.manager.ClothManager
import com.pinocchio.santaclothes.apiserver.notification.service.NotificationService
import com.pinocchio.santaclothes.apiserver.support.toLocalDateTime
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

private const val STRING_ABBREVIATE_LENGTH = 20

@Service
class ViewService(
    private val userService: UserService,
    private val noticeService: NoticeService,
    private val clothService: ClothService,
    private val analysisRequestService: AnalysisRequestService,
    private val imageService: ImageService,
    private val notificationService: NotificationService,
) {
    private val clothManager: ClothManager = ClothManager()

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
        if (analysisRequest.status != AnalysisStatus.DONE && analysisRequest.status != AnalysisStatus.NOTIFIED) {
            throw NoSuchElementException("$requestId is not exists")
        }

        val manageTip = clothManager.howTo(cloth)
        val howToTitle = manageTip.title
        val howToContent = manageTip.content

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
            createdDate = Instant.now().toLocalDateTime()
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
                        requestAt = it.createdAt.toLocalDateTime()
                    )
                }
                .sortedBy { it.requestAt }
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

    fun clothView(accessToken: UUID, clothId: Long): AnalysisRequestView {
        val analysisRequestId = analysisRequestService.getByClothId(clothId).id!!
        return analysisRequestView(accessToken, analysisRequestId)
    }

    fun CareLabel.toDetail(): List<CareLabelDetail> {
        return listOf(
            CareLabelDetail(
                waterType.imageUrl,
                "물세탁",
                waterType.description
            ),
            CareLabelDetail(
                dryType.imageUrl,
                "건조",
                dryType.description
            ),
            CareLabelDetail(
                dryCleaning.imageUrl,
                "드라이클리닝",
                dryCleaning.description
            ),
            CareLabelDetail(
                bleachType.imageUrl,
                "표백",
                bleachType.description
            ),
            CareLabelDetail(
                ironingType.imageUrl,
                "다림질",
                ironingType.description
            )
        )
    }
}
