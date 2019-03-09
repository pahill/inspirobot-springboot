package com.pamelaahill.inspirobot.controller

import com.pamelaahill.inspirobot.entity.Inspiration
import com.pamelaahill.inspirobot.entity.Tag
import com.pamelaahill.inspirobot.repository.ImageRepository
import com.pamelaahill.inspirobot.repository.InspirationRepository
import com.pamelaahill.inspirobot.repository.TagRepository
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.util.*

private const val INSPIROBOT_URL = "http://inspirobot.me/api?generate=true"
private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36"

@RestController
@RequestMapping("/api/users/{userId}")
class InspirationController(private val inspirationRepository: InspirationRepository, private val imageRepository: ImageRepository, private val tagRepository: TagRepository) {

    @PostMapping("/inspirations")
    fun create(@PathVariable("userId", required = true) userId: Long): Inspiration? {
        val inspirobotURL = getInspirobotURL() ?: return null
        val inspirobotImage = getInspirobotImage(inspirobotURL) ?: return null
        val fileURL = imageRepository.save(inspirobotURL.substringAfterLast("."), inspirobotImage.inputStream())
        return inspirationRepository.save(Inspiration(userId, fileURL))
    }

    @PutMapping("/inspirations/{inspirationId}")
    fun update(@PathVariable("userId", required = true) userId: Long,
               @PathVariable("inspirationId", required = true) inspirationId: Long,
               @RequestBody(required = true) tags: List<String>): Inspiration? {
        val inspiration = inspirationRepository.findByIdOrNull(inspirationId) ?: return null
        inspiration.tags = tags.map { title ->
            val tag = tagRepository.findByTitle(title).firstOrNull()
            if (tag == null) {
                tagRepository.save(Tag(title))
            } else {
                tag
            }
        }
        return inspirationRepository.save(inspiration)
    }

    @GetMapping("/inspirations/{inspirationId}")
    fun findById(@PathVariable("userId", required = true) userId: Long,
                 @PathVariable("inspirationId", required = true) inspirationId: Long): Inspiration? {
        return inspirationRepository.findByIdOrNull(inspirationId)
    }

    @GetMapping("/inspirations/{inspirationId}/images")
    fun findImage(@PathVariable("userId", required = true) userId: Long,
                  @PathVariable("inspirationId", required = true) inspirationId: Long): ResponseEntity<ByteArrayResource>? {
        val inspiration = inspirationRepository.findByIdOrNull(inspirationId) ?: return null
        val file = imageRepository.findImageById(inspiration.fileURL)
        val byteArray = file?.readBytes() ?: return null
        val resource = ByteArrayResource(byteArray)
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource)
    }

    @GetMapping("/inspirations")
    fun findBy(@PathVariable("userId", required = true) userId: Long,
               @RequestParam("tagId", required = false) tagId: Long?): List<Inspiration> {
        return if (tagId == null) {
            inspirationRepository.findByUserId(userId)
        } else {
            val tag = tagRepository.findByIdOrNull(tagId) ?: return emptyList()
            inspirationRepository.findByTags(tag)
        }
    }

    private fun getInspirobotURL(): String? {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders().apply {
            accept = Arrays.asList(MediaType.TEXT_HTML)
            add("user-agent", USER_AGENT)
        }
        val entity = HttpEntity("parameters", headers)
        return restTemplate.exchange(URI(INSPIROBOT_URL), HttpMethod.GET, entity, String::class.java).body
    }

    private fun getInspirobotImage(inspirobotURL: String): ByteArray? {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders().apply {
            accept = Arrays.asList(MediaType.IMAGE_JPEG)
            add("user-agent", USER_AGENT)
        }
        val entity = HttpEntity("parameters", headers)
        return restTemplate.exchange(URI(inspirobotURL), HttpMethod.GET, entity, ByteArray::class.java).body
    }
}