package com.writerai.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object BlogTable : IntIdTable() {
    val userId: Column<String> = varchar("userId", 50)
    val title: Column<String> = varchar("title", 50)
    val description: Column<String> = varchar("description", 100)
    val content: Column<String> = varchar("content", 2000)
    val timeStamp: Column<Long> = long("timeStamp")
}