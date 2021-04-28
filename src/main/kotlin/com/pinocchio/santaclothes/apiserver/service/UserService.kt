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
    fun register(token: UUID, name: String, accountType: AccountType): UserToken {
        userRepository.findById(token).ifPresent {
            throw DatabaseException(ExceptionReason.DUPLICATE_ENTITY)
        }

        return userRepository.insert(User(token = token, name = name, accountType = accountType)).let {
            userTokenRepository.save(
                UserToken(
                    accessToken = UUID.randomUUID(),
                    refreshToken = UUID.randomUUID(),
                    userToken = it.token
                )
            )
        }
    }

    fun login(accessToken: UUID) {
        val userToken = userTokenRepository.findByAccessToken(accessToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.USER_TOKEN_NOT_EXISTS) }

        if (userToken.expiredAt.isBefore(Instant.now())) {
            throw TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN)
        }
    }
}
