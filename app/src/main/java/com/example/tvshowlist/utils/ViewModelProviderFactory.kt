package com.example.tvshowlist.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tvshowlist.MainViewModel
import com.example.tvshowlist.domain.repositories.TvShowsRepository

class ViewModelProviderFactory(private val repository: TvShowsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}