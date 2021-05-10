package com.pinocchio.santaclothes.apiserver.config.interceptor

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.exception.ExceptionReason
import com.pinocchio.santaclothes.apiserver.exception.TokenInvalidException
import com.pinocchio.santaclothes.apiserver.support.authorizationToUuid
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SecurityInterceptor(
    private val tokenManager: TokenManager
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

        authorizationToUuid(authorization).run {
            tokenManager.validateAccessToken(this)
        }

        return true
    }


    companion object {
        private val permitHosts: List<String> = listOf(
            "127.0.0.1",
            "3.139.60.119",
            "0:0:0:0:0:0:0:1"
        )
    }
}
