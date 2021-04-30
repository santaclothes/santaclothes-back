package com.pinocchio.santaclothes.apiserver.config.interceptor

import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.service.UserService
import org.springframework.web.servlet.HandlerInterceptor
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SecurityInterceptor(
    private val userService: UserService
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (permitHosts.contains(request.remoteHost)) {
            return true
        }
        val authorization = request.getHeader("Authorization")
            ?: throw TokenInvalidException(ExceptionReason.INVALID_ACCESS_TOKEN)

        val accessToken = UUID.fromString(authorization.substring(TOKEN_PREFIX))

        userService.validateAccessToken(accessToken)
        return true
    }

    companion object {
        private const val TOKEN_PREFIX = 7 // BEARER
        private val permitHosts: List<String> = listOf(
            "127.0.0.1",
            "3.139.60.119",
            "0:0:0:0:0:0:0:1"
        )
    }
}
