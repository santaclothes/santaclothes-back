package com.pinocchio.santaclothes.apiserver.controller.dto

import com.pinocchio.santaclothes.apiserver.entity.Notice
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import java.time.Instant

data class HomeView(
    val userName: String,
    val totalClothesCount: Long,
    val notices: List<Notice>,
    val hasNewNotification: Boolean
)

data class AnalysisRequestView(
    val clothName: String,
    val howToTitle: String,
    val howToContent: String,
    val clothImageUrl: String?,
    val careLabelImageUrls: List<String>,
    val careLabelDetails: List<CareLabelDetail>,
    val createdDate: Instant
)

data class CareLabelDetail(val iconUrl: String, val name: String, val description: String)

data class MyPageView(
    val userName: String,
    val myClothesCount: Long,
    val myPageClothes: List<MyPageCloth>
)

data class MyPageCloth(val clothId: Long, val clothType: ClothesType, val imageUrl: String, val requestAt: Instant)
