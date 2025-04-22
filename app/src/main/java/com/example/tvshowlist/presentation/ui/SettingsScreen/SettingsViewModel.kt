package com.example.tvshowlist.presentation.ui.SettingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshowlist.domain.repositories.SettingsRepository
import com.example.tvshowlist.domain.usecases.GetPotentialSpoilerEnabledUseCase
import com.example.tvshowlist.domain.usecases.SetPotentialSpoilerEnabledUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getPotentialSpoilerEnabledUseCase: GetPotentialSpoilerEnabledUseCase,
    private val setPotentialSpoilerEnabledUseCase: SetPotentialSpoilerEnabledUseCase
) : ViewModel() {

    val isPotentialSpoilerCensorshipEnabled = getPotentialSpoilerEnabledUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )


    fun setPotentialSpoilerEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            setPotentialSpoilerEnabledUseCase(isEnabled)
        }
    }
}