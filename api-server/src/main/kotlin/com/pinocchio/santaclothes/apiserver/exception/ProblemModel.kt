package com.pinocchio.santaclothes.apiserver.exception

import lombok.Value

@Value
class ProblemModel {
    var title: String? = null
    var status = 0
    var reason: ExceptionReason? = null
}
