package com.pinocchio.santaclothes.apiserver.test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.pinocchio.santaclothes.apiserver.test.WireMockPostProcessor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.ConfigurableEnvironment
import java.util.Random

@Configuration
class WireMockPostProcessor : EnvironmentPostProcessor, InitializingBean {
    companion object {
        private var WIRE_MOCK: WireMock
        private var WIRE_MOCK_SERVER: WireMockServer
        private val LOG = LoggerFactory.getLogger(WireMockPostProcessor::class.java)

        init {
            val defaultPort = 10000
            val random = Random()
            var port: Int = defaultPort + random.nextInt(3001)
            val max: Int = port + 1000
            var wireMockServer: WireMockServer? = null
            while (port < max) {
                try {
                    wireMockServer = WireMockServer(
                        WireMockConfiguration.options()
                            .port(port)
                    )
                    wireMockServer.start()
                    break
                } catch (e: Exception) {
                    LOG.info("WireMockServer start failed. port : {}", port)
                }
                port++
            }
            if (wireMockServer == null || !wireMockServer.isRunning()) {
                throw RuntimeException("wireMock is not running")
            }
            WIRE_MOCK_SERVER = wireMockServer
            WIRE_MOCK = WireMock(WIRE_MOCK_SERVER)
            Runtime.getRuntime().addShutdownHook(
                Thread {
                    try {
                        WIRE_MOCK_SERVER.stop()
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            )
        }
    }

    override fun afterPropertiesSet() {
        System.setProperty("wiremock.server.port", WIRE_MOCK_SERVER.port().toString())
    }

    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        WireMock.configureFor(WIRE_MOCK)
    }
}
