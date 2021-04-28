package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.entity.UserToken
import com.pinocchio.santaclothes.apiserver.exception.DatabaseException
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import com.pinocchio.santaclothes.apiserver.repository.UserTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userTokenRepository: UserTokenRepository
) {
    fun register(token: UUID, name: String, accountType: AccountType) {
        userRepository.findById(token).ifPresent {
            throw DatabaseException(ExceptionReason.DUPLICATE_ENTITY)
        }

        userRepository.insert(User(token = token, name = name, accountType = accountType)).let {
            userTokenRepository.save(
                UserToken(
                    accessToken = UUID.randomUUID(),
                    refreshToken = UUID.randomUUID(),
                    userToken = it.token
                )
            )
        }
    }

    fun login(userToken: UUID): UserToken {
        val authentication =
            userTokenRepository.findByUserToken(userToken).orElseGet { UserToken(userToken = userToken) }

        if (authentication.expiredAt.isBefore(Instant.now())) {
            throw TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN)
        }

        return authentication
    }

    fun refresh(refreshToken: UUID): UserToken {
        val refreshedToken = userTokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_REFRESH_TOKEN) }

        return userTokenRepository.save(UserToken(userToken = refreshedToken.userToken, refreshToken = refreshToken))
    }
}
