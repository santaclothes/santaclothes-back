package com.pinocchio.santaclothes.apiserver.controller.dto

import com.pinocchio.santaclothes.apiserver.entity.Notice

data class HomeView(val userName: String, val clothesCount: Long, val notices: List<Notice>, val hasNewNotification: Boolean)
