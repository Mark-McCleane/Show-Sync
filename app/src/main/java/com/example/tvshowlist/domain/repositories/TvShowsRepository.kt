package com.example.tvshowlist.domain.repositories

import com.example.tvshowlist.data.entities.getTvShow.GetTvShowApiResponse
import com.example.tvshowlist.data.entities.getTvShowSeason.GetSeasonApiResponse
import com.example.tvshowlist.data.entities.search.SearchApiResponse
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes
import kotlinx.coroutines.flow.Flow

interface TvShowsRepository {
    suspend fun getTVShows(query: String = ""): SearchApiResponse
    suspend fun getTVShowById(tvShowId: Int): GetTvShowApiResponse
    suspend fun getTvShowSeason(tvShowId: Int, seasonNumber: Int = 1): GetSeasonApiResponse
    suspend fun getTop10TvShowEpisodesById(tvShowId: Int): List<TvShowSeasonEpisodes>
    suspend fun insertTvShow(tvShow: TvShowSeasonEpisodes)
    suspend fun insertRecentTvShow(tvShow: TvShow)
    suspend fun getRecentTvShows(): Flow<List<TvShow>>
    suspend fun updateIsWatchedStatus(episodeId: Int, isWatchedStatus: Boolean)
    suspend fun getIsWatchedStatus(episodeId: Int): Boolean
    suspend fun getTvShowSeasonOffline(
        tvShowId: Int,
        seasonSelected: Int
    ): List<TvShowSeasonEpisodes>
}
