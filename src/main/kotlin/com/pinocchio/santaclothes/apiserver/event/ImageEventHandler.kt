package com.pinocchio.santaclothes.apiserver.event

import com.pinocchio.santaclothes.apiserver.support.FileSupports
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.io.File

@Component
class ImageEventHandler {
    @Async
    @EventListener
    fun uploadEvent(command: ImageUploadCommand) {
        FileSupports.createImageFolderIfNotExists()
        val transferFile = File(command.filePath)
        command.file.transferTo(transferFile)
    }
}
