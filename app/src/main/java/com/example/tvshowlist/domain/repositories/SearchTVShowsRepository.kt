package com.example.tvshowlist.domain.repositories

import com.example.tvshowlist.data.db.TvShowCheckerDao
import com.example.tvshowlist.data.entities.getTvShow.GetTvShowApiResponse
import com.example.tvshowlist.data.entities.getTvShowSeason.GetSeasonApiResponse
import com.example.tvshowlist.data.entities.search.SearchApiResponse
import com.example.tvshowlist.data.remote.RetrofitInstance
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes


class SearchTVShowsRepository(private val dao: TvShowCheckerDao) {
    suspend fun getTVShows(query: String = ""): SearchApiResponse =
        RetrofitInstance.api.searchTvShowsByName(query = query)

    suspend fun getTVShowById(tvShowId: Int): GetTvShowApiResponse =
        RetrofitInstance.api.getTvShowById(id = tvShowId)

    suspend fun getTvShowSeason(tvShowId: Int, seasonNumber: Int = 1): GetSeasonApiResponse =
        RetrofitInstance.api.getSeasonById(tvId = tvShowId, seasonNumber = seasonNumber)

    suspend fun insertTvShow(tvShow: TvShowSeasonEpisodes) = dao.upsertTvShowChecker(tvShowChecker = tvShow)
    suspend fun getAllTvShowsSeasonEpisodes(): List<TvShowSeasonEpisodes> = dao.getAllTvShowsSeasonEpisodes()

    suspend fun updateIsWatchedStatus(episodeId: Int, isWatchedStatus: Boolean) =
        dao.updateIsWatchedStatus(episodeId = episodeId, isWatchedStatus = isWatchedStatus)

    suspend fun getIsWatchedStatus(episodeId: Int): Boolean = dao.getIsWatchedStatus(episodeId = episodeId)
}