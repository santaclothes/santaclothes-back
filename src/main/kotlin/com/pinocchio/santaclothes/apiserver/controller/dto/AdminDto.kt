package com.pinocchio.santaclothes.apiserver.controller.dto

import com.pinocchio.santaclothes.apiserver.entity.CareLabel
import com.pinocchio.santaclothes.apiserver.entity.type.BleachType
import com.pinocchio.santaclothes.apiserver.entity.type.DryCleaning
import com.pinocchio.santaclothes.apiserver.entity.type.DryType
import com.pinocchio.santaclothes.apiserver.entity.type.IroningType
import com.pinocchio.santaclothes.apiserver.entity.type.WaterType

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
