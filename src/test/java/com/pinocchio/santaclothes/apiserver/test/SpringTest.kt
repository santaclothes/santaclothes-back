package com.pinocchio.santaclothes.apiserver.test

import com.pinocchio.santaclothes.apiserver.ApiServerApplication
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(classes = [ApiServerApplication::class])
@ContextConfiguration(classes = [SpringTest.TestConfig::class])
@ActiveProfiles("test")
abstract class SpringTest {
    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun applicationEventPublisher(publisher: ApplicationEventPublisher): ApplicationEventPublisher? {
            return Mockito.spy(publisher)
        }
    }
}
