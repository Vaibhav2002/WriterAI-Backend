package com.writerai.di

import com.writerai.controllers.UserController
import com.writerai.data.datasources.user.UserDataSource
import com.writerai.data.datasources.user.UserDataSourceImpl
import com.writerai.data.repo.UserRepo
import org.koin.dsl.module

val userModule = module {
    single<UserDataSource> {
        UserDataSourceImpl()
    }

    single {
        UserRepo(get())
    }

    single { UserController(get()) }
}