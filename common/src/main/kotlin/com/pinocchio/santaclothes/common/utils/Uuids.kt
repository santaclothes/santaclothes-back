package com.pinocchio.santaclothes.common.utils

import java.util.UUID


class Uuids {
    companion object {
        fun generateUuidString(): String = UUID.randomUUID().toString()
    }
}
