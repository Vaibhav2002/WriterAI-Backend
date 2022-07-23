package com.writerai.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object ShareTable : IntIdTable() {
    val ownerId: Column<String> = varchar("ownerId", 50)
    val sharedTo: Column<String> = varchar("sharedTo", 50)
    val sharedToEmail:Column<String> = varchar("sharedToEmail", 50)
    val blogId: Column<Int> = integer("blogId")
}