package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.controller.dto.AnalysisRequestView
import com.pinocchio.santaclothes.apiserver.controller.dto.HomeView
import com.pinocchio.santaclothes.apiserver.controller.dto.MyPageView
import com.pinocchio.santaclothes.apiserver.controller.dto.ReportView
import com.pinocchio.santaclothes.apiserver.service.ViewService
import com.pinocchio.santaclothes.apiserver.support.authorizationToUuid
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["View"])
@RestController
@RequestMapping("/view")
class ViewController(private val viewService: ViewService) {
    @ApiOperation("메인화면")
    @GetMapping("/home")
    fun home(
        @ApiParam(hidden = true)
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String
    ): HomeView =
        viewService.homeView(authorizationToUuid(authorization))

    @ApiOperation("분석 요청 결과화면")
    @GetMapping("/analysisRequest/{requestId}")
    fun getRequest(
        @PathVariable("requestId") resultId: Long,
        @ApiParam(hidden = true)
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String
    ): AnalysisRequestView =
        viewService.analysisRequestView(authorizationToUuid(authorization), resultId)

    @ApiOperation("옷 분석 결과 화면")
    @GetMapping("/cloth/{clothId}")
    fun getCloth(
        @PathVariable("clothId") clothId: Long,
        @ApiParam(hidden = true)
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String
    ): AnalysisRequestView =
        viewService.clothView(authorizationToUuid(authorization), clothId)

    @ApiOperation("마이페이지 화면")
    @GetMapping("/myPage")
    fun getMyPage(
        @ApiParam(hidden = true)
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String
    ): MyPageView = viewService.myPageView(authorizationToUuid(authorization))

    @ApiOperation("레포트 화면")
    @GetMapping("/analysisRequest/{requestId}/report")
    fun checkRequest(
        @PathVariable("requestId") requestId: Long,
        @ApiParam(hidden = true)
        @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String
    ): ReportView = viewService.reportView(requestId)
}
