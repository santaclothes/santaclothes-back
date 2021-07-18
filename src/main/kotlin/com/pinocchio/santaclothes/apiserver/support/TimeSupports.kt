package com.pinocchio.santaclothes.apiserver.support

import com.pinocchio.santaclothes.apiserver.support.TimeSupports.Companion.ZONE_ID
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

class TimeSupports {
    companion object {
        val LOCALE: Locale = Locale.KOREA
        val ZONE_ID: ZoneId = ZoneId.of("Asia/Seoul")
    }
}

fun Instant.toLocalDateTime() = this.atZone(ZONE_ID).toLocalDateTime()
