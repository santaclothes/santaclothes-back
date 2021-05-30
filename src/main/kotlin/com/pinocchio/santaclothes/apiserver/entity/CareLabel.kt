package com.pinocchio.santaclothes.apiserver.entity

import com.pinocchio.santaclothes.apiserver.entity.type.*
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("CARE_LABEL")
data class CareLabel(
    @Id @Column("ID") var id: Long? = null,
    @Column("CLOTH_ID") var clothId: Long? = null,
    @Column("WATER_TYPE") val waterType: WaterType,
    @Column("BLEACH_TYPE") val bleachType: BleachType,
    @Column("DRY_TYPE") val dryType: DryType,
    @Column("DRY_CLEANING") val dryCleaning: DryCleaning,
    @Column("IRONING_TYPE") val ironingType: IroningType,
)
