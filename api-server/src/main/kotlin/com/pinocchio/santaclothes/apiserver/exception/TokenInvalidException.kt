package com.pinocchio.santaclothes.apiserver.exception

class TokenInvalidException(reason: ExceptionReason) : AttributeException(reason) {
    init {
        if (reason == ExceptionReason.USER_TOKEN_NOT_EXISTS) {
            with("ErrorCode", "invalid_client")
        } else {
            with("ErrorCode", "invalid_request")
        }
    }
}
