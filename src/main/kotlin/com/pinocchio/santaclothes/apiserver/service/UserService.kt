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
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userTokenRepository: UserTokenRepository
) {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun register(token: String, name: String, accountType: AccountType) =
        if (userRepository.findById(token).isPresent)
            throw DatabaseException(ExceptionReason.DUPLICATE_ENTITY)
        else
            userRepository.insert(User(token = token, name = name, accountType = accountType))


    fun login(userToken: String, deviceToken: String): UserToken =
        userTokenRepository.findFirstByUserTokenOrderByCreatedAtDesc(userToken)
            .orElseGet { userTokenRepository.save(UserToken(userToken = userToken, deviceToken = deviceToken)) }
            .apply {
                if (this.isExpired(Instant.now())) {
                    throw TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN)
                }

                if (this.deviceToken != this.deviceToken) {
                    userTokenRepository.save(this.copy(deviceToken = this.deviceToken))
                }
            }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun refresh(refreshToken: UUID): UserToken {
        return userTokenRepository.findFirstByRefreshTokenOrderByCreatedAtDesc(refreshToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_REFRESH_TOKEN) }
            .run {
                userTokenRepository.save(
                    UserToken(
                        userToken = this.userToken,
                        deviceToken = this.deviceToken,
                        refreshToken = refreshToken
                    )
                )
            }
    }

    fun validateAccessToken(accessToken: UUID) {
        val authorization = userTokenRepository.findFirstByAccessTokenOrderByCreatedAtDesc(accessToken)
            .orElseThrow { TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN) }

        if (authorization.isExpired(Instant.now())) {
            throw TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN)
        }
    }
}
