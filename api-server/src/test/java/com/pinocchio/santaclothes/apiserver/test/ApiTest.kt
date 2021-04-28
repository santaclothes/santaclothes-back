package com.pinocchio.santaclothes.apiserver.test

import com.pinocchio.santaclothes.apiserver.ApiServerApplication
import io.restassured.RestAssured
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(classes = [ApiServerApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiTest {
    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @LocalServerPort
    private val port = 0

    @BeforeEach
    fun setUp() {
        RestAssured.reset()
        RestAssured.port = port
        // RestAssured.config()
        // 	.objectMapperConfig(
        // 		new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> JSON_MAPPER)
        // 	);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext)
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @AfterEach
    fun tearDown() {
        RestAssured.reset()
    }
}
