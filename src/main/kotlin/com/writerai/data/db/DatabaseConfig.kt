package com.writerai.data.db

import com.writerai.data.db.DatabaseSecrets.PASSWORD
import com.writerai.data.db.DatabaseSecrets.URL
import com.writerai.data.db.DatabaseSecrets.USERNAME
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    val db by lazy {
        Database.connect(
            url = URL,
            driver = "com.mysql.cj.jdbc.Driver",
            user = USERNAME,
            password = PASSWORD,
        )
    }
}
