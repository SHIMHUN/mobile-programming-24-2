package com.example.todolist

import android.app.Application
import com.example.todolist.di.databaseModule
import com.example.todolist.di.repositoryModule
import com.example.todolist.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            androidLogger()
            modules(databaseModule, repositoryModule, viewModelModule)
        }
    }
}