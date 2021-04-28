package com.pinocchio.santaclothes.apiserver.support

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

object DateSupports {
    private const val EXPIRE_AMOUNT = 1
    private val EXPIRE_UNIT: TemporalUnit = ChronoUnit.DAYS
    fun calculateExpireDate(now: Instant): Instant = now.plus(EXPIRE_AMOUNT.toLong(), EXPIRE_UNIT)
}
