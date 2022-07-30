package com.writerai.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object ShareTable : IntIdTable() {
    val ownerId: Column<String> = varchar("ownerId", 100)
    val sharedTo: Column<String> = varchar("sharedTo", 100)
    val sharedToEmail:Column<String> = varchar("sharedToEmail", 500)
    val projectId: Column<Int> = integer("projectId")
}