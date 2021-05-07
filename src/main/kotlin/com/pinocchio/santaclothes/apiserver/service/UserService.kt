package com.pinocchio.santaclothes.apiserver.service

import com.pinocchio.santaclothes.apiserver.entity.AccountType
import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.exception.DatabaseException
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
) {
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun register(token: String, name: String, accountType: AccountType) =
        if (userRepository.findById(token).isPresent)
            throw DatabaseException(ExceptionReason.DUPLICATE_ENTITY)
        else
            userRepository.insert(User(token = token, name = name, accountType = accountType))
}
