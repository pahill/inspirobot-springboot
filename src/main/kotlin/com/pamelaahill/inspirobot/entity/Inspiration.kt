package com.pamelaahill.inspirobot.entity

import java.net.URL
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "inspirations")
data class Inspiration(val userId: Long,
                       val fileURL: URL,
                       @ManyToMany(
                               fetch = FetchType.LAZY,
                               cascade = [CascadeType.PERSIST, CascadeType.MERGE]
                       )
                       @JoinTable(
                               name = "post_tags",
                               joinColumns = [JoinColumn(name = "post_id")],
                               inverseJoinColumns = [JoinColumn(name = "tag_id")]
                       )
                       var tags: List<Tag> = mutableListOf(),
                       val generateDate: Long = Date().time
) {
    @Id
    @GeneratedValue
    var id: Long = 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return id == (other as Inspiration).id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
