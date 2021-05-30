package com.pinocchio.santaclothes.apiserver.controller

import com.pinocchio.santaclothes.apiserver.entity.Image
import com.pinocchio.santaclothes.apiserver.service.ImageService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.io.FileInputStream
import javax.servlet.http.HttpServletResponse

@Api(tags = ["Image"])
@RestController
@RequestMapping("/image")
class ImageController(
    @Autowired val imageService: ImageService
) {
    @GetMapping("/{fileName}")
    @ResponseStatus(HttpStatus.OK)
    fun render(@PathVariable fileName: String, response: HttpServletResponse) {
        imageService.getImageByFileName(fileName).writeTo(response)
    }

    fun Image.writeTo(response: HttpServletResponse) {
        response.setHeader("Content-Disposition", "attachment; filename=\"${this.fileName}\";")
        response.setHeader("Content-Transfer-Encoding", "binary")
        response.setHeader("Content-Type", "application/octet-stream")
        response.setHeader("Pragma", "no-cache;")
        response.setHeader("Expires", "-1;")

        FileInputStream(this.filePath).readBytes().also {
            response.outputStream.write(it)
        }
    }
}