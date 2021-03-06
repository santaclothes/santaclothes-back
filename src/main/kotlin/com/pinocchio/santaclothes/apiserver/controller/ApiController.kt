package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.controller.dto.AnalysisRequestForm
import com.pinocchio.santaclothes.apiserver.controller.dto.AnalysisRequestResult
import com.pinocchio.santaclothes.apiserver.controller.dto.AnalysisStatusRequest
import com.pinocchio.santaclothes.apiserver.controller.dto.NotificationElement
import com.pinocchio.santaclothes.apiserver.controller.dto.NotificationList
import com.pinocchio.santaclothes.apiserver.controller.dto.toAnalysisRequestDocument
import com.pinocchio.santaclothes.apiserver.notification.service.NotificationService
import com.pinocchio.santaclothes.apiserver.service.AnalysisRequestService
import com.pinocchio.santaclothes.apiserver.service.UserService
import com.pinocchio.santaclothes.apiserver.support.authorizationToUuid
import com.pinocchio.santaclothes.apiserver.support.toLocalDateTime
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Api"])
@RestController
@RequestMapping("/api")
class ApiController(
    private val analysisRequestService: AnalysisRequestService,
    private val notificationService: NotificationService,
    private val userService: UserService
) {
    @ApiOperation("분석 요청")
    @PostMapping("/analysisRequest", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.CREATED)
    fun makeRequest(
        request: AnalysisRequestForm,
        @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String
    ): AnalysisRequestResult {
        val saved = analysisRequestService.save(
            accessToken = authorizationToUuid(authorization),
            request.toAnalysisRequestDocument()
        )
        return AnalysisRequestResult(saved.id!!)
    }

    @ApiOperation("분석 요청 상태 변경")
    @PutMapping("/analysisRequest/{analysisRequestId}")
    fun changeStatus(
        @PathVariable analysisRequestId: Long,
        @RequestBody analysisStatusRequest: AnalysisStatusRequest
    ) {
        analysisRequestService.withStatus(analysisRequestId, analysisStatusRequest.status)
    }

    @ApiOperation("알람 리스트 조회")
    @GetMapping("/notification")
    fun notifications(
        @ApiParam(hidden = true)
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String,
        @ApiParam("page") page: Long,
        @ApiParam("size") size: Long
    ): NotificationList {
        val user = userService.findByAccessToken(authorizationToUuid(authorization)).orElseThrow()
        val notificationElements = notificationService.findNewByUserTokenWithPaging(user.token).map {
            NotificationElement(
                id = it.id!!,
                analysisRequestId = it.analysisRequestId,
                clothName = it.clothName,
                requestAt = it.createdAt.toLocalDateTime()
            )
        }

        return NotificationList(user.name, notificationElements)
    }
}
