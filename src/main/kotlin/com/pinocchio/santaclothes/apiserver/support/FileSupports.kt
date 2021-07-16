package com.pinocchio.santaclothes.apiserver.support

import java.io.File

class FileSupports {
    companion object {
        fun createImageFolderIfNotExists() = File(FILE_PREFIX_URL).run { if (!exists()) mkdirs() }

        fun resolvePath(vararg filePaths: String): String =
            StringBuilder().let {
                it.append(FILE_PREFIX_URL)
                it.append(File.separator)
                for (filePath in filePaths) {
                    it.append(filePath)
                    it.append(File.separator)
                }
                it.toString()
            }

        private val FILE_PREFIX_URL = System.getProperty("PINOCCHIO_IMAGES_PATH")
    }
}
