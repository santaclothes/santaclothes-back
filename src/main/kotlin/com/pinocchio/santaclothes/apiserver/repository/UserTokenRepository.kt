package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.UserToken
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface UserTokenRepository : CrudRepository<UserToken, Long> {
    fun findFirstByUserTokenOrderByCreatedAtDesc(userToken: UUID): Optional<UserToken>

    fun findFirstByRefreshTokenOrderByCreatedAtDesc(refreshToken: UUID): Optional<UserToken>
}
