package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.entity.Notification
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.notification.service.NotificationService
import com.pinocchio.santaclothes.apiserver.service.AnalysisRequestDocument
import com.pinocchio.santaclothes.apiserver.service.AnalysisRequestService
import com.pinocchio.santaclothes.apiserver.service.UserService
import com.pinocchio.santaclothes.apiserver.support.authorizationToUuid
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Api(tags = ["Api"])
@RestController
@RequestMapping("/api")
class ApiController(
    @Autowired val analysisRequestService: AnalysisRequestService,
    @Autowired val notificationService: NotificationService,
    @Autowired val userService: UserService
) {
    @ApiOperation("분석 요청")
    @PostMapping("/analysisRequest", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.CREATED)
    fun makeRequest(
        request: AnalysisRequestForm,
        @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String
    ): AnalysisRequestResult {
        with(request) {
            val saved = analysisRequestService.save(
                AnalysisRequestDocument(
                    accessToken = authorizationToUuid(authorization),
                    clothName = clothName,
                    clothesColor = clothColor,
                    clothesType = clothType,
                    clothImage = request.clothImage,
                    labelImage = request.labelImages
                )
            )
            return AnalysisRequestResult(saved.id!!)
        }
    }

    @ApiOperation("알람 리스트 조회")
    @GetMapping("/notification")
    fun notifications(
        @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String
    ): List<Notification> {
        val userToken = userService.findByAccessToken(authorizationToUuid(authorization)).orElseThrow().token
        return notificationService.findByUserToken(userToken)
    }

    @ApiOperation("알람 조회")
    @GetMapping("/notification/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun notifications(
        @ApiParam(hidden = true)
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String,
        @PathVariable id: Long
    ) {
        notificationService.view(id)
    }
}

data class AnalysisRequestForm(
    val clothImage: MultipartFile,
    val labelImages: MultipartFile,
    val clothName: String,
    val clothType: ClothesType,
    val clothColor: ClothesColor,
)

data class AnalysisRequestResult(
    val analysisRequestId: Long,
)
