package com.example.tvshowlist

import android.app.Application
import com.example.tvshowlist.di.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TvShowCheckerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TvShowCheckerApplication)
            modules(AppModules.mainModule, AppModules.databaseModule)
        }
    }
}