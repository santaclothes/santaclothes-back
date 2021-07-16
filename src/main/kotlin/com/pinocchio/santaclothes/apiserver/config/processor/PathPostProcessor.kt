package com.pinocchio.santaclothes.apiserver.config.processor

import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.util.ObjectUtils

class PathPostProcessor : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment?, application: SpringApplication?) {
        val systemImageProperty = System.getProperty("PINOCCHIO_IMAGES_PATH")
        val springEnvironmentProperty = environment!!.getProperty("pinocchio.images.path")
        val javaTempPath = System.getProperty("java.io.tmpdir") + "/images"

        if (!ObjectUtils.isEmpty(systemImageProperty)) {
            return
        }
        if (!ObjectUtils.isEmpty(springEnvironmentProperty)) {
            System.setProperty("PINOCCHIO_IMAGES_PATH", springEnvironmentProperty!!)
            return
        }
        System.setProperty("PINOCCHIO_IMAGES_PATH", javaTempPath)
    }
}
