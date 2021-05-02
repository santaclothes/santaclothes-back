package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.test.SpringDataTest
import org.springframework.beans.factory.annotation.Autowired

class ImageRepositoryTest(@Autowired val sut: ImageRepository) : SpringDataTest()
