package com.example.tvshowlist.domain.usecases

import com.example.tvshowlist.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetPotentialSpoilerEnabledUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean> = repository.isPotentialSpoilerCensorshipEnabled()
}