package com.pamelaahill.inspirobot.repository

import org.springframework.context.annotation.Bean
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URL

interface ImageRepository {
    fun save(fileExtension: String, inputStream: ByteArrayInputStream): URL

    fun findImageById(fileURL: URL): File?

}