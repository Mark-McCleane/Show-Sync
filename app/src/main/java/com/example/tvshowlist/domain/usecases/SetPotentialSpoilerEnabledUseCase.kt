package com.example.tvshowlist.domain.usecases

import com.example.tvshowlist.domain.repositories.SettingsRepository

class SetPotentialSpoilerEnabledUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(enabled: Boolean) =
        repository.setPotentialSpoilerCensorshipEnabled(enabled)
}