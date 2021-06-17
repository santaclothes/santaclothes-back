package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.User
import com.pinocchio.santaclothes.apiserver.repository.custom.WithInsert
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository : CrudRepository<User, String>, WithInsert<User> {
    @Query(
        """SELECT u."TOKEN", u."NAME", u."ACCOUNT_TYPE" 
        FROM "USER" u LEFT JOIN "AUTHORIZATION_TOKEN" at on u."TOKEN" = at."USER_TOKEN"
        WHERE at."ACCESS_TOKEN" = :accessToken"""
    )
    fun findByAccessToken(accessToken: UUID): Optional<User>
}
