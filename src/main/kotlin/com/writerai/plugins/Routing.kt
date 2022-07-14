package com.writerai.plugins

import com.writerai.routes.blogRoutes
import com.writerai.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            userRoutes()
            blogRoutes()
        }
    }
}
