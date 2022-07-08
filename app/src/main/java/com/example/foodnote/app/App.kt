package com.example.foodnote.app

import android.app.Application
import com.example.foodnote.di.applicationModule
import com.example.foodnote.di.authScreenModule
import com.example.foodnote.di.calorieCalculatorScreenModule
import com.example.foodnote.di.dataStoreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    applicationModule,
                    calorieCalculatorScreenModule,
                    dataStoreModule,
                    authScreenModule
                )
            )
        }
    }
}