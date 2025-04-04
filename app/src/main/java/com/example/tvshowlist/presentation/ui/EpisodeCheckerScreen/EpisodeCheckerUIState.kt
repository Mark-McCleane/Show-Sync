package com.example.tvshowlist.presentation.ui.EpisodeCheckerScreen

import com.example.tvshowlist.domain.model.TvShowExtended
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes

data class EpisodeCheckerUIState(
    val tvShow: TvShowExtended? = null,
    val seasonEpisodes: List<TvShowSeasonEpisodes> = listOf(),
    val isEpisodesLoaded: Boolean = false,
    val top10Episodes: List<TvShowSeasonEpisodes> = listOf(),
    val error: String = ""
)
