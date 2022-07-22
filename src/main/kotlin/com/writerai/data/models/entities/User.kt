package com.writerai.data.models.entities

import com.writerai.data.db.tables.UserTable
import com.writerai.data.models.response.UserResponse
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class User(id: EntityID<String>) : Entity<String>(id), Model<UserResponse> {
    companion object : EntityClass<String, User>(UserTable)

    var username by UserTable.username
    var email by UserTable.email
    var totalApiReqMade by UserTable.totalApiReqMade

    override fun toResponse() = UserResponse(
        id = id._value.toString(),
        username, email, totalApiReqMade
    )
}