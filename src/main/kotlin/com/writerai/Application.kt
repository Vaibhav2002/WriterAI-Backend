package com.writerai

import com.writerai.data.db.DatabaseConfig
import com.writerai.plugins.*
import com.writerai.plugins.auth.FirebaseAdmin
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    FirebaseAdmin.init()
    configureDatabase()
    configureAuthentication()
    configureDependencyInjection()
    configureRouting()
    configureSerialization()
    configureMonitoring()
}

private fun Application.configureDatabase() {
    val url = environment.config.property("ktor.database.url").getString()
    val username = environment.config.property("ktor.database.username").getString()
    val password = environment.config.property("ktor.database.password").getString()
    DatabaseConfig.init(url, username, password)
}