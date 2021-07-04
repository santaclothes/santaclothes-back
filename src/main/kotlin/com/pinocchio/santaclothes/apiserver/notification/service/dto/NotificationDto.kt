package com.pinocchio.santaclothes.apiserver.notification.service.dto

import com.fasterxml.jackson.annotation.JsonProperty


data class FirebaseMessageWrapper(val message: FirebaseMessage) {
    constructor(token: String, title: String, body: String, image: String?) : this(
        message = FirebaseMessage(
            token,
            NotificationContent(title, body, image)
        )
    )

    data class FirebaseMessage(
        val token: String,
        @JsonProperty("notification") val notificationContent: NotificationContent
    )

    data class NotificationContent(val title: String, val body: String, val image: String?)
}

data class NotificationResponse(val name: String)




