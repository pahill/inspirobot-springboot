package com.pamelaahill.inspirobot.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "tags")
class Tag(val title: String,
          @JsonIgnore
          @ManyToMany(
                  fetch = FetchType.LAZY,
                  cascade = [CascadeType.PERSIST, CascadeType.MERGE],
                  mappedBy = "tags"
          )
          var inspirations: List<Inspiration> = mutableListOf()
) {
    @Id
    @GeneratedValue
    var id: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return  (id != (other as Tag).id)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

