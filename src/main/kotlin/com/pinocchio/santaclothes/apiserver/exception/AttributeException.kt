package com.pinocchio.santaclothes.apiserver.exception

open class AttributeException : RuntimeException {
    val attributes: MutableMap<String, String> = mutableMapOf()
    val reason: ExceptionReason

    constructor(reason: ExceptionReason) : super() {
        this.reason = reason
        with("reason", reason.name)
    }

    constructor(cause: Throwable?, reason: ExceptionReason) : super(cause) {
        this.reason = reason
        with("reason", reason.name)
    }

    fun with(key: String, value: String): AttributeException {
        attributes[key] = value
        return this
    }
}
