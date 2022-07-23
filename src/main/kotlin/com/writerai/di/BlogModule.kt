package com.writerai.di

import com.writerai.controllers.BlogController
import com.writerai.data.datasources.blog.BlogDataSource
import com.writerai.data.datasources.blog.BlogDataSourceImpl
import com.writerai.data.repo.BlogRepo
import org.koin.dsl.module

val blogModule = module {
    single<BlogDataSource> {
        BlogDataSourceImpl()
    }

    single {
        BlogRepo(get(), get())
    }

    single { BlogController(get(), get()) }
}