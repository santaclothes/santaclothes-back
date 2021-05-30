package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.Image
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface ImageRepository : CrudRepository<Image, Long> {
    @Query(
        "SELECT \"IMAGE_ID\", \"FILE_PATH\", \"THUMBNAIL_PATH\", \"TYPE\", \"USER_TOKEN\", \"CLOTH_ID\" FROM \"IMAGE\" " +
                "WHERE \"TYPE\" = \'CARE_LABEL\' and \"CARE_LABEL_ID\" is null"
    )
    fun findAllCareLabelsToProcess(): List<Image>

    @Query(
        "SELECT \"IMAGE_ID\", \"FILE_PATH\", \"THUMBNAIL_PATH\", \"TYPE\", \"USER_TOKEN\", \"CLOTH_ID\" FROM \"IMAGE\"" +
                "WHERE \"TYPE\" = \'CARE_LABEL\' and \"CARE_LABEL_ID\" is null and \"IMAGE_ID\" = :id"
    )
    fun findCareLabelById(id: Long): Optional<Image>
}
