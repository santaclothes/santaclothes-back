package com.pinocchio.santaclothes.apiserver.support

import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import java.util.UUID


private const val TOKEN_PREFIX = 7 // BEARER

fun authorizationToUuid(authorization: String): UUID =
    if (authorization.length <= 7) throw TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN)
    else UUID.fromString(authorization.substring(TOKEN_PREFIX))
