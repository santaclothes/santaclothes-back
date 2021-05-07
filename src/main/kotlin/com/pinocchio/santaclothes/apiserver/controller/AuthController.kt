package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.authorization.TokenManager
import com.pinocchio.santaclothes.apiserver.controller.dto.LoginRequest
import com.pinocchio.santaclothes.apiserver.controller.dto.RefreshRequest
import com.pinocchio.santaclothes.apiserver.controller.dto.RegisterRequest
import com.pinocchio.santaclothes.apiserver.entity.UserToken
import com.pinocchio.santaclothes.apiserver.exception.ProblemModel
import com.pinocchio.santaclothes.apiserver.service.UserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@Api(tags = ["Auth"])
@RestController
@RequestMapping("/auth")
class AuthController(
    @Autowired val userService: UserService,
    @Autowired val tokenManager: TokenManager
) {
    @ApiOperation("회원가입")
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "회원가입 성공"),
            ApiResponse(code = 400, message = "요청 파라미터 오류", response = ProblemModel::class),
            ApiResponse(code = 409, message = "이미 존재하는 회원 입니다", response = ProblemModel::class)
        ]
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody registerRequest: @Valid RegisterRequest): Unit =
        with(registerRequest) {
            userService.register(userToken, name, accountType)
        }

    @ApiOperation("로그인")
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "로그인 성공"),
            ApiResponse(code = 400, message = "존재하지 않는 소셜 아이디", response = ProblemModel::class)
        ]
    )
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody loginRequest: @Valid LoginRequest): UserToken =
        tokenManager.acquireAccessToken(loginRequest.userToken, loginRequest.deviceToken)

    @ApiOperation("인증 토큰 갱신")
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "토큰 갱신 성공"),
            ApiResponse(code = 400, message = "존재하지 않거나 만료된 리프레시 토큰", response = ProblemModel::class)
        ]
    )
    @PutMapping("/accessToken")
    fun refresh(@RequestBody request: @Valid RefreshRequest): UserToken =
        tokenManager.refreshAccessToken(request.refreshToken)
}
