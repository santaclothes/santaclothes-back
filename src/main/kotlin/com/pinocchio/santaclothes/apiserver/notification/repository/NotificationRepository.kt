package com.pinocchio.santaclothes.apiserver.notification.repository

import com.pinocchio.santaclothes.apiserver.entity.Notification
import com.pinocchio.santaclothes.apiserver.entity.NotificationCategory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface NotificationRepository : CrudRepository<Notification, Long> {
    fun findByUserTokenAndCategory(
        userToken: String,
        category: NotificationCategory,
        pageable: Pageable = PageRequest.of(0, 20)
    ): List<Notification>

    fun findFirstByUserTokenAndNewOrderByCreatedAtDesc(userToken: String, new: Boolean): Optional<Notification>
}
