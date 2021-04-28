package com.pinocchio.santaclothes.apiserver.exception

enum class ExceptionReason {
    INVALID_REFRESH_TOKEN,
    INVALID_ACCESS_TOKEN,
    USER_TOKEN_NOT_EXISTS,
    DUPLICATE_ENTITY
}
