package com.writerai.data.models.entities

import com.writerai.data.db.tables.BlogTable
import com.writerai.data.models.response.BlogResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Blog(id: EntityID<Int>) : IntEntity(id), Model<BlogResponse> {
    companion object : IntEntityClass<Blog>(BlogTable)

    var title by BlogTable.title
    var description by BlogTable.description
    var content by BlogTable.content
    var timeStamp by BlogTable.timeStamp
    var user by User referencedOn BlogTable.user

    override fun toResponse() = BlogResponse(
        id = id.value,
        title = title,
        description = description,
        content = content,
        timeStamp = timeStamp,
        userId = user.id.value
    )
}