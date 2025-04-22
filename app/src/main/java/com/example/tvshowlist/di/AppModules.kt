package com.example.tvshowlist.di

import com.example.tvshowlist.data.db.TvShowCheckerDatabase
import com.example.tvshowlist.data.repository.SettingsRepositoryImpl
import com.example.tvshowlist.domain.repositories.TvShowsRepository
import com.example.tvshowlist.data.repository.TvShowsRepositoryImpl
import com.example.tvshowlist.domain.repositories.SettingsRepository
import com.example.tvshowlist.domain.usecases.GetPotentialSpoilerEnabledUseCase
import com.example.tvshowlist.domain.usecases.SetPotentialSpoilerEnabledUseCase
import com.example.tvshowlist.presentation.MainViewModel
import com.example.tvshowlist.presentation.ui.SettingsScreen.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppModules {
    companion object {
        val mainModule = module {
            single<TvShowsRepository> { TvShowsRepositoryImpl(get()) }
            viewModel { MainViewModel(get()) }
        }

        val settingsModule = module {
            single<SettingsRepository> { SettingsRepositoryImpl(androidContext()) }
            factory { GetPotentialSpoilerEnabledUseCase(get()) }
            factory { SetPotentialSpoilerEnabledUseCase(get()) }
            viewModel { SettingsViewModel(get(), get()) }
        }

        val databaseModule = module {
            single { TvShowCheckerDatabase.getDatabase(get()).tvShowCheckerDao() }
        }


    }
}