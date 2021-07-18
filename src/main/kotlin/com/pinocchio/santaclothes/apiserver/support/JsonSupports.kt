package com.pinocchio.santaclothes.apiserver.support

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.pinocchio.santaclothes.apiserver.support.TimeSupports.Companion.LOCALE
import com.pinocchio.santaclothes.apiserver.support.TimeSupports.Companion.ZONE_ID
import org.zalando.problem.ProblemModule
import org.zalando.problem.violations.ConstraintViolationProblemModule
import java.util.TimeZone

class JsonSupports {
    companion object {
        val JSON_MAPPER: ObjectMapper = ObjectMapper().registerModules(
            JavaTimeModule(),
            ProblemModule(),
            KotlinModule(),
            ConstraintViolationProblemModule()
        )
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setTimeZone(TimeZone.getTimeZone(ZONE_ID))
            .setLocale(LOCALE)

        fun <T> toJsonString(value: T): String = JSON_MAPPER.writeValueAsString(value)
    }
}
