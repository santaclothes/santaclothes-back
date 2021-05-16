package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.Image
import org.springframework.data.repository.CrudRepository

interface ImageRepository : CrudRepository<Image, Long>
