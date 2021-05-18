package com.pinocchio.santaclothes.apiserver.controller.dto

import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.entity.type.*

data class CareLabelIcon(
    val waterType: WaterType,
    val bleachType: BleachType,
    val dryType: DryType,
    val dryCleaning: DryCleaning,
    val ironingType: IroningType
)

fun CareLabelIcon.toCareLabel() = CareLabel(
    waterType = waterType,
    bleachType = bleachType,
    dryType = dryType,
    dryCleaning = dryCleaning,
    ironingType = ironingType
)
