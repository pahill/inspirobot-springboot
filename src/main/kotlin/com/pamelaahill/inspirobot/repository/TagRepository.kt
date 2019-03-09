package com.pamelaahill.inspirobot.repository

import com.pamelaahill.inspirobot.entity.Tag
import org.springframework.data.repository.CrudRepository

interface TagRepository : CrudRepository<Tag, Long> {
    fun findByTitle(title: String): List<Tag>
}
