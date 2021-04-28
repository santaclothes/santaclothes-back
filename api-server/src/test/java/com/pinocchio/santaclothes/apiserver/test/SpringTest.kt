package com.pinocchio.santaclothes.apiserver.test

import com.pinocchio.santaclothes.apiserver.ApiServerApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [ApiServerApplication::class])
@ActiveProfiles("test")
abstract class SpringTest 
