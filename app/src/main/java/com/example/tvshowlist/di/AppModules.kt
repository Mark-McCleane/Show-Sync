package com.example.tvshowlist.di

import com.example.tvshowlist.data.db.TvShowCheckerDatabase
import com.example.tvshowlist.domain.repositories.TvShowsRepository
import com.example.tvshowlist.domain.repositories.TvShowsRepositoryImpl
import com.example.tvshowlist.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppModules {
    companion object {
        val mainModule = module {
            single<TvShowsRepository> { TvShowsRepositoryImpl(get()) }
            viewModel { MainViewModel(get()) }
        }
        val databaseModule = module {
            single { TvShowCheckerDatabase.getDatabase(get()).tvShowCheckerDao() }
        }
    }
}