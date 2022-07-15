package com.writerai.data.db

import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    private lateinit var url: String
    private lateinit var username: String
    private lateinit var password: String

    fun init(url:String, username:String, password:String){
        this.url = url
        this.username = username
        this.password = password
        db
    }

    private val db by lazy {
        Database.connect(
            url = url,
            driver = "com.mysql.cj.jdbc.Driver",
            user = username,
            password = password,
        )
    }
}
