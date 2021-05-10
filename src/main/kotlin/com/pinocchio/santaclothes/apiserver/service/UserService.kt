package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.entity.UserToken
import com.pinocchio.santaclothes.apiserver.exception.DatabaseException
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val tokenManager: TokenManager,
) {
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun register(token: String, name: String, accountType: AccountType) =
        if (userRepository.findById(token).isPresent)
            throw DatabaseException(ExceptionReason.DUPLICATE_ENTITY)
        else
            userRepository.insert(User(token = token, name = name, accountType = accountType))

    fun login(userToken: String, deviceToken: String): UserToken =
        userRepository.findById(userToken).orElseThrow { TokenInvalidException(ExceptionReason.USER_TOKEN_NOT_EXISTS) }
            .run {
                tokenManager.acquireAccessToken(userToken, deviceToken)
            }
}
