package com.example.tvshowlist.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.model.TvShowExtended
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes

data class EpisodeCheckerUIState(
    val tvShow: TvShowExtended? = null,
    val seasonEpisodes: List<TvShowSeasonEpisodes> = listOf(),
    val isEpisodesLoaded: Boolean = false,
    val error: String = ""
)
