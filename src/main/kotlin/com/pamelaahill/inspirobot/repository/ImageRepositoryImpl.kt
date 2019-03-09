package com.pamelaahill.inspirobot.repository

import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URL
import java.util.*

private const val IMAGE_DIR = "images"

@Component
class ImageRepositoryImpl : ImageRepository {
    override fun save(fileExtension: String, inputStream: ByteArrayInputStream): URL {
        val inspirationImage = File("$IMAGE_DIR/${UUID.randomUUID()}.$fileExtension")
        inspirationImage.writeBytes(inputStream.readBytes())
        return inspirationImage.toURI().toURL()
    }

    override fun findImageById(fileURL: URL): File? {
        return File(fileURL.toURI())
    }
}