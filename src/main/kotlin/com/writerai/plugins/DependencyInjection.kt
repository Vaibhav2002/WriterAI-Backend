package com.writerai.plugins

import com.writerai.di.projectModule
import com.writerai.di.sharedToModule
import com.writerai.di.userModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(userModule, projectModule, sharedToModule)
    }
}
