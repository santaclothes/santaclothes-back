package com.pinocchio.santaclothes.apiserver.notification.repository

import com.pinocchio.santaclothes.apiserver.entity.Notification
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface NotificationRepository : CrudRepository<Notification, Long> {
    fun findByUserToken(userToken: String): List<Notification>

    fun findFirstByUserTokenAndNewOrderByCreatedAtDesc(userToken: String, new: Boolean): Optional<Notification>
}
