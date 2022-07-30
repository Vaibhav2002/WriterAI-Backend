package com.writerai.data.db.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object UserTable : IdTable<String>() {
    val email: Column<String> = varchar("email", 500).uniqueIndex()
    val username: Column<String> = varchar("username", 100)
    val totalApiReqMade: Column<Int> = integer("totalApiReqMade")
    override val id: Column<EntityID<String>> = varchar("id", 100).entityId()
    override val primaryKey = PrimaryKey(id)
}