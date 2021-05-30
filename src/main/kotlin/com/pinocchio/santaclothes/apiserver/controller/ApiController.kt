package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.service.AnalysisRequestDocument
import com.pinocchio.santaclothes.apiserver.service.AnalysisRequestService
import com.pinocchio.santaclothes.apiserver.support.authorizationToUuid
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Api(tags = ["Api"])
@RestController
@RequestMapping("/api")
class ApiController(@Autowired val analysisRequestService: AnalysisRequestService) {
    @ApiOperation("분석 요청")
    @PostMapping("/analysisRequest", consumes = ["multipart/form-data"])
    @ResponseStatus(HttpStatus.CREATED)
    fun makeRequest(
        request: AnalysisRequestForm,
        @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String
    ) {
        with(request) {
            analysisRequestService.save(
                AnalysisRequestDocument(
                    accessToken = authorizationToUuid(authorization),
                    clothName = clothName,
                    clothesColor = clothColor,
                    clothesType = clothType,
                    clothImage = request.clothImage,
                    labelImage = request.labelImages
                )
            )
        }
    }
}

data class AnalysisRequestForm(
    val clothImage: MultipartFile,
    val labelImages: MultipartFile,
    val clothName: String,
    val clothType: ClothesType,
    val clothColor: ClothesColor,
)
