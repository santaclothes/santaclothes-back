package com.pinocchio.santaclothes.apiserver.repository

import com.pinocchio.santaclothes.apiserver.entity.AnalysisRequest
import org.springframework.data.repository.CrudRepository

interface AnalysisRequestRepository : CrudRepository<AnalysisRequest, Long>
