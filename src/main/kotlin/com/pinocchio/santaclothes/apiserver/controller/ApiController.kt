package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.controller.dto.HomeView
import com.pinocchio.santaclothes.apiserver.service.ViewService
import com.pinocchio.santaclothes.apiserver.support.authorizationToUuid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api")
class ApiController(@Autowired val viewService: ViewService) {
    @GetMapping("/home")
    fun home(@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) authorization: String): HomeView =
        viewService.homeView(authorizationToUuid(authorization))
}
