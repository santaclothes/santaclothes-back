package com.pinocchio.santaclothes.apiserver.exception

import java.lang.RuntimeException
import java.util.NoSuchElementException
import org.zalando.problem.ProblemBuilder
import org.zalando.problem.spring.web.advice.ProblemHandling
import java.lang.IllegalStateException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status
import java.lang.Exception

abstract class CommonGlobalExceptionHandler : ProblemHandling {
    // 400 BadRequest
    // 500 InternalServerError
    // 403 Forbidden
    // Service unavailable
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        exception: IllegalStateException?,
        nativeWebRequest: NativeWebRequest?
    ): ResponseEntity<Problem> {
        return createBadRequestProblem(exception, nativeWebRequest)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(exception: RuntimeException?, request: NativeWebRequest?): ResponseEntity<Problem> {
        return createInternalServerProblem(exception, request)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchException(exception: NoSuchElementException?, request: NativeWebRequest?): ResponseEntity<Problem> {
        return createNotFoundProblem(exception, request)
    }

    protected fun createNotFoundProblem(
        e: NoSuchElementException?,
        request: NativeWebRequest?
    ): ResponseEntity<Problem> {
        val problem: Problem = Problem.builder()
            .withTitle(Status.NOT_FOUND.reasonPhrase)
            .withStatus(Status.NOT_FOUND)
            .build()
        return create(e, problem, request)
    }

    protected fun createInternalServerProblem(e: Exception?, request: NativeWebRequest?): ResponseEntity<Problem> {
        val problem: Problem = Problem.builder()
            .withTitle(Status.INTERNAL_SERVER_ERROR.reasonPhrase)
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .build()
        return create(e, problem, request)
    }

    protected fun createBadRequestProblem(e: Exception?, request: NativeWebRequest?): ResponseEntity<Problem> {
        val problem: Problem = Problem.builder()
            .withTitle(Status.BAD_REQUEST.reasonPhrase)
            .withStatus(Status.BAD_REQUEST)
            .build()
        return create(e, problem, request)
    }

    protected fun applyAttribute(builder: ProblemBuilder, e: AttributeException) {
        e.attributes.forEach { (key: String?, value: String?) -> builder.with(key, value) }
    }
}
