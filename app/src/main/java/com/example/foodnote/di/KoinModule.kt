package com.example.foodnote.di

import com.example.foodnote.data.base.RetrofitImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationModule = module {

    // Получаем сервис
    single(named(NAME_SERVICE_SARAWAN)) {
        get<RetrofitImpl>(
            qualifier = named(NAME_DATASOURCE_REMOTE)
        ).getService()
    }
}