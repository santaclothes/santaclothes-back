package com.pinocchio.santaclothes.apiserver.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status

@ControllerAdvice
class GlobalExceptionHandler : CommonGlobalExceptionHandler() {
    @ExceptionHandler(EventInvalidException::class)
    fun handleEventInvalidException(
        exception: EventInvalidException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> = createEventProblem(exception, request)

    @ExceptionHandler(DatabaseException::class)
    fun handleDatabaseException(
        exception: DatabaseException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> = createDuplicateProblem(exception, request)

    @ExceptionHandler(TokenInvalidException::class)
    fun handleTokenInvalidException(
        exception: TokenInvalidException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> = createTokenInvalidProblem(exception, request)

    protected fun createEventProblem(e: EventInvalidException, request: NativeWebRequest): ResponseEntity<Problem> {
        val status = REASON_STATUS_MAP[e.reason] ?: Status.BAD_REQUEST
        val builder = Problem.builder()
            .withTitle(status.reasonPhrase)
            .withStatus(status)
        applyAttribute(builder, e)
        val problem: Problem = builder.build()
        return create(e, problem, request)
    }

    protected fun createDuplicateProblem(e: DatabaseException, request: NativeWebRequest): ResponseEntity<Problem> {
        val status = REASON_STATUS_MAP[e.reason] ?: Status.BAD_REQUEST
        val builder = Problem.builder()
            .withTitle(status.reasonPhrase)
            .withStatus(status)
        applyAttribute(builder, e)
        val problem: Problem = builder.build()
        return create(e, problem, request)
    }

    protected fun createTokenInvalidProblem(
        e: TokenInvalidException,
        request: NativeWebRequest
    ): ResponseEntity<Problem> {
        val status = REASON_STATUS_MAP[e.reason] ?: Status.UNAUTHORIZED
        val builder = Problem.builder()
            .withTitle(status.reasonPhrase)
            .withStatus(status)
        applyAttribute(builder, e)
        val problem: Problem = builder.build()
        return create(e, problem, request)
    }

    companion object {
        val REASON_STATUS_MAP: Map<ExceptionReason, Status> = mapOf(
            ExceptionReason.DUPLICATE_ENTITY to Status.CONFLICT,
            ExceptionReason.USER_TOKEN_NOT_EXISTS to Status.BAD_REQUEST,
            ExceptionReason.INVALID_REFRESH_TOKEN to Status.BAD_REQUEST
        )
    }
}
