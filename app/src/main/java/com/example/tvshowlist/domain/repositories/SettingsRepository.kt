package com.example.tvshowlist.domain.repositories

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun setPotentialSpoilerCensorshipEnabled(isEnabled: Boolean)

    fun isPotentialSpoilerCensorshipEnabled() : Flow<Boolean>
}