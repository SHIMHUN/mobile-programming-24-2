package com.example.todolist.di

import com.example.todolist.data.db.TodoDao
import com.example.todolist.data.db.TodoDatabase
import com.example.todolist.data.repository.Repository
import com.example.todolist.data.repository.RepositoryImpl
import com.example.todolist.ui.TodoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single<TodoDao> {
        get<TodoDatabase>().todoDao()
    }

    single<TodoDatabase> {
        TodoDatabase.getInstance(androidContext())
    }
}

val viewModelModule = module {
    viewModel { TodoViewModel(get()) }
}

val repositoryModule = module {
    single<Repository> { RepositoryImpl(get()) }
}