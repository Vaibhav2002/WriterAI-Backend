package com.writerai.data.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object ProjectTable : IntIdTable() {
    val userId: Column<String> = varchar("userId", 100)
    val title: Column<String> = varchar("title", 5000)
    val description: Column<String> = varchar("description", 5000)
    val content: Column<String> = text("content")
    val coverPic:Column<String> = varchar("coverPic", 1000)
    val timeStamp: Column<Long> = long("timeStamp")
}