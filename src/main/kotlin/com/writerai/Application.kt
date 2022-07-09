package com.writerai

import com.writerai.data.db.DatabaseConfig.db
import com.writerai.plugins.configureDependencyInjection
import com.writerai.plugins.configureMonitoring
import com.writerai.plugins.configureRouting
import com.writerai.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    db
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureDependencyInjection()
        configureRouting()
        configureSerialization()
        configureMonitoring()
    }.start(wait = true)
}
