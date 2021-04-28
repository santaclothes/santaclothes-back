package com.pinocchio.santaclothes.apiserver.exception

class EventInvalidException : AttributeException {
    constructor(eventId: String, reason: ExceptionReason) : super(reason) {
        with("eventId", eventId)
    }

    constructor(cause: Throwable, eventId: String, reason: ExceptionReason) : super(cause, reason) {
        with("eventId", eventId)
    }
}
