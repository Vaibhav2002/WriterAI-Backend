package com.writerai.data.db.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object UserTable : IdTable<String>() {
    val email: Column<String> = varchar("email", 30).uniqueIndex()
    val username: Column<String> = varchar("username", 40)
    override val id: Column<EntityID<String>> = varchar("id", 50).entityId().autoIncrement()
    override val primaryKey = PrimaryKey(id)
}