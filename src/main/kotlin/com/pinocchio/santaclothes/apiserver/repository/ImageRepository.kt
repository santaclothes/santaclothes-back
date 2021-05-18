package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.Image
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface ImageRepository : CrudRepository<Image, Long> {
    @Query(
        "SELECT image_id, file_path, thumbnail_path, image_type, image_status, user_token FROM IMAGE " +
                "WHERE image_type = \'CARE_LABEL\' and image_status = \'PROCESS\'"
    )
    fun findAllCareLabelsToProcess(): List<Image>

    @Query(
        "SELECT image_id, file_path, thumbnail_path, image_type, image_status, user_token FROM IMAGE " +
                "WHERE image_type = \'CARE_LABEL\' and image_status = 'PROCESS' and image_id = :id"
    )
    fun findCareLabelById(id: Long): Optional<Image>
}
