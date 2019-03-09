package com.pamelaahill.inspirobot.repository

import com.pamelaahill.inspirobot.entity.Inspiration
import com.pamelaahill.inspirobot.entity.Tag
import org.springframework.data.repository.CrudRepository

interface InspirationRepository : CrudRepository<Inspiration, Long> {
    fun findByUserId(userId: Long): List<Inspiration>

    fun findByTags(tag: Tag): List<Inspiration>
}