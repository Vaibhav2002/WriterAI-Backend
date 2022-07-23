package com.writerai.di

import com.writerai.controllers.ProjectController
import com.writerai.data.datasources.project.ProjectDataSource
import com.writerai.data.datasources.project.ProjectDataSourceImpl
import com.writerai.data.repo.ProjectRepo
import org.koin.dsl.module

val projectModule = module {
    single<ProjectDataSource> {
        ProjectDataSourceImpl()
    }

    single {
        ProjectRepo(get(), get())
    }

    single { ProjectController(get(), get()) }
}