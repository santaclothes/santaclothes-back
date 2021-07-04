package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.Image
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface ImageRepository : CrudRepository<Image, Long> {
    @Query(
        "SELECT \"IMAGE_ID\", \"FILE_NAME\", \"FILE_PATH\", \"FILE_URL\", \"THUMBNAIL_PATH\", \"TYPE\"," +
            " \"USER_TOKEN\", \"CLOTH_ID\" FROM \"IMAGE\" " +
            "WHERE \"TYPE\" = \'CARE_LABEL\' and \"CARE_LABEL_ID\" is null"
    )
    fun findAllCareLabelsToProcess(): List<Image>

    @Query(
        "SELECT \"IMAGE_ID\",\"FILE_NAME\", \"FILE_PATH\", \"FILE_URL\", \"THUMBNAIL_PATH\", \"TYPE\", " +
            "\"USER_TOKEN\", \"CLOTH_ID\" FROM \"IMAGE\"" +
            "WHERE \"TYPE\" = \'CARE_LABEL\' and \"CARE_LABEL_ID\" is null and \"IMAGE_ID\" = :imageId"
    )
    fun findNotClassifiedCareLabelImageByImageId(imageId: Long): Optional<Image>

    @Query(
        "SELECT \"IMAGE_ID\",\"FILE_NAME\", \"FILE_PATH\", \"FILE_URL\", \"THUMBNAIL_PATH\", \"TYPE\", " +
            "\"USER_TOKEN\", \"CLOTH_ID\" FROM \"IMAGE\"" +
            "WHERE \"TYPE\" = \'CARE_LABEL\' and \"CARE_LABEL_ID\" = :careLabelId"
    )
    fun findImageByCareLabelId(careLabelId: Long): Optional<Image>

    fun findByClothId(clothId: Long): List<Image>

    fun findByFileName(fileName: String): Optional<Image>
}
