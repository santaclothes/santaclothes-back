package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import com.pinocchio.santaclothes.apiserver.entity.AnalysisStatus
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface AnalysisRequestRepository : CrudRepository<AnalysisRequest, Long> {
    @Query(
        """SELECT ar."ID", ar."USER_TOKEN", ar."STATUS", ar."CREATED_AT", 
                c."CLOTH_ID" as "cloth_CLOTH_ID", c."NAME" as "cloth_NAME", c."COLOR" as "cloth_COLOR",
                c."ANALYSIS_REQUEST_ID" as "cloth_ANALYSIS_REQUEST_ID", c."TYPE" as "cloth_TYPE",
                c."USER_TOKEN" as "cloth_USER_TOKEN", c."CREATED_AT" as "cloth_CREATED_AT",
                cl."ID" as "cloth_careLabel_ID", cl."WATER_TYPE" as "cloth_careLabel_WATER_TYPE", 
                cl."BLEACH_TYPE" as "cloth_careLabel_BLEACH_TYPE", cl."DRY_TYPE" as "cloth_careLabel_DRY_TYPE",
                cl."DRY_CLEANING" as "cloth_careLabel_DRY_CLEANING", 
                cl."IRONING_TYPE" as "cloth_careLabel_IRONING_TYPE", cl."CLOTH_ID" as "cloth_careLabel_CLOTH_ID"
                FROM "ANALYSIS_REQUEST" ar LEFT JOIN "CLOTH" c ON ar."ID" = c."ANALYSIS_REQUEST_ID"
                LEFT JOIN "CARE_LABEL" cl ON c."CLOTH_ID" = cl."CLOTH_ID"
                WHERE c."CLOTH_ID" = :clothId """
    )
    fun findByClothId(clothId: Long): Optional<AnalysisRequest>

    @Query(
        """SELECT ar."ID", ar."USER_TOKEN", ar."STATUS", ar."CREATED_AT", 
                c."CLOTH_ID" as "cloth_CLOTH_ID", c."NAME" as "cloth_NAME", c."COLOR" as "cloth_COLOR",
                c."ANALYSIS_REQUEST_ID" as "cloth_ANALYSIS_REQUEST_ID", c."TYPE" as "cloth_TYPE",
                c."USER_TOKEN" as "cloth_USER_TOKEN", c."CREATED_AT" as "cloth_CREATED_AT",
                cl."ID" as "cloth_careLabel_ID", cl."WATER_TYPE" as "cloth_careLabel_WATER_TYPE", 
                cl."BLEACH_TYPE" as "cloth_careLabel_BLEACH_TYPE", cl."DRY_TYPE" as "cloth_careLabel_DRY_TYPE",
                cl."DRY_CLEANING" as "cloth_careLabel_DRY_CLEANING", 
                cl."IRONING_TYPE" as "cloth_careLabel_IRONING_TYPE", cl."CLOTH_ID" as "cloth_careLabel_CLOTH_ID"
                FROM "ANALYSIS_REQUEST" ar LEFT JOIN "CLOTH" c ON ar."ID" = c."ANALYSIS_REQUEST_ID"
                LEFT JOIN "CARE_LABEL" cl ON c."CLOTH_ID" = cl."CLOTH_ID"
                WHERE c."USER_TOKEN" = :userToken """
    )
    fun findByUserToken(userToken: String): List<AnalysisRequest>

    fun findByIdAndStatus(clothId: Long, status: AnalysisStatus): Optional<AnalysisRequest>
}
