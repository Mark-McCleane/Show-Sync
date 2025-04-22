package com.example.tvshowlist.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.tvshowlist.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {
    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

    private val SPOILER_CENSORSHIP_KEY =
        booleanPreferencesKey("is_potential_spoiler_censorship")


    override suspend fun setPotentialSpoilerCensorshipEnabled(isEnabled: Boolean) {
        context._dataStore.edit { preferences ->
            preferences[SPOILER_CENSORSHIP_KEY] = isEnabled
        }
    }

    override fun isPotentialSpoilerCensorshipEnabled(): Flow<Boolean> {
        return context._dataStore.data.map { preferences ->
            preferences[SPOILER_CENSORSHIP_KEY] ?: true
        }
    }
}