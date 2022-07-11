package com.writerai

import com.writerai.data.db.DatabaseConfig.db
import com.writerai.plugins.*
import com.writerai.plugins.auth.FirebaseAdmin
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    db
    FirebaseAdmin.init()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureAuthentication()
        configureDependencyInjection()
        configureRouting()
        configureSerialization()
        configureMonitoring()
    }.start(wait = true)
}
