package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.Image
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface ImageRepository : CrudRepository<Image, Long> {
    @Query(
        "SELECT image_id, file_path, thumbnail_path, type, user_token, cloth_id FROM IMAGE " +
                "WHERE type = \'CARE_LABEL\' and care_label_id is null"
    )
    fun findAllCareLabelsToProcess(): List<Image>

    @Query(
        "SELECT image_id, file_path, thumbnail_path, type, user_token, cloth_id FROM IMAGE " +
                "WHERE type = \'CARE_LABEL\' and care_label_id is null and image_id = :id"
    )
    fun findCareLabelById(id: Long): Optional<Image>
}
