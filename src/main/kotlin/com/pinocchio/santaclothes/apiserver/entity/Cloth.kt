package com.pinocchio.santaclothes.apiserver.entity

import com.pinocchio.santaclothes.apiserver.entity.type.BleachType
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesColor
import com.pinocchio.santaclothes.apiserver.entity.type.ClothesType
import com.pinocchio.santaclothes.apiserver.entity.type.DryCleaning
import com.pinocchio.santaclothes.apiserver.entity.type.DryType
import com.pinocchio.santaclothes.apiserver.entity.type.IroningType
import com.pinocchio.santaclothes.apiserver.entity.type.WaterType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("CLOTH")
data class Cloth(
    @Id @Column("CLOTH_ID") var id: Long? = null,
    @Column("NAME") val name: String,
    @Column("COLOR") val color: ClothesColor,
    @Column("TYPE") val type: ClothesType,
    @Column("USER_TOKEN") var userToken: String,
    @MappedCollection(idColumn = "CLOTH_ID") var careLabel: CareLabel? = null,
    @Column("CREATED_AT") val createdAt: Instant = Instant.now()
)

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
