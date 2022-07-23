package com.writerai.di

import com.writerai.data.datasources.sharedTo.SharedToDataSource
import com.writerai.data.datasources.sharedTo.SharedToDatasourceImpl
import org.koin.dsl.module

val sharedToModule = module {
    single<SharedToDataSource> {
        SharedToDatasourceImpl()
    }
}