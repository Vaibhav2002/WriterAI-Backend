package com.writerai.data.models.entities

import com.writerai.data.db.tables.ProjectTable
import com.writerai.data.models.response.ProjectResponse
import com.writerai.data.models.response.SharedToResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Project(id: EntityID<Int>) : IntEntity(id), Model<ProjectResponse> {
    companion object : IntEntityClass<Project>(ProjectTable)

    var title by ProjectTable.title
    var description by ProjectTable.description
    var content by ProjectTable.content
    var timeStamp by ProjectTable.timeStamp
    var userId by ProjectTable.userId
    var coverPic by ProjectTable.coverPic

    override fun toResponse() = ProjectResponse(
        id = id.value,
        title = title,
        description = description,
        content = content,
        timeStamp = timeStamp,
        coverPic = coverPic,
        userId = userId,
    )

    fun toResponse(sharedTo:List<SharedToResponse>) = ProjectResponse(
        id = id.value,
        title = title,
        description = description,
        content = content,
        timeStamp = timeStamp,
        coverPic = coverPic,
        userId = userId,
        sharedTo = sharedTo
    )
}