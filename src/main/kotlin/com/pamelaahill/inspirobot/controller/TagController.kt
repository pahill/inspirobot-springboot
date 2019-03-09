package com.pamelaahill.inspirobot.controller

import com.pamelaahill.inspirobot.entity.Tag
import com.pamelaahill.inspirobot.repository.TagRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
class TagController(private val tagRepository: TagRepository) {

    @GetMapping
    fun find(@RequestParam("title", required = false) title: String?): List<Tag> {
        return if (title == null) {
            tagRepository.findAll().toList()
        } else {
            tagRepository.findByTitle(title)
        }
    }

}