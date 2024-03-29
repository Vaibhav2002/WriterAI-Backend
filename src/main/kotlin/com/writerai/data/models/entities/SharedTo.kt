package com.writerai.data.models.entities

import com.writerai.data.db.tables.ShareTable
import com.writerai.data.models.response.SharedToResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SharedTo(id: EntityID<Int>) : IntEntity(id), Model<SharedToResponse> {
    companion object : IntEntityClass<SharedTo>(ShareTable)

    var ownerId by ShareTable.ownerId
    var sharedTo by ShareTable.sharedTo
    var sharedToEmail by ShareTable.sharedToEmail
    var projectId by ShareTable.projectId

    override fun toResponse() = SharedToResponse(
        projectId = projectId,
        email = sharedToEmail,
        sharedId = id.value
    )
}