package com.pinocchio.santaclothes.apiserver.controller.dto

import com.pinocchio.santaclothes.apiserver.entity.Notice
import java.time.Instant

data class HomeView(
    val userName: String,
    val clothesCount: Long,
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