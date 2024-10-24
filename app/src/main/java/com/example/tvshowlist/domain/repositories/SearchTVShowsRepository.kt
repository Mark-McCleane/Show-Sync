package com.example.tvshowlist.domain.repositories

import com.example.tvshowlist.data.entities.getTvShow.GetTvShowApiResponse
import com.example.tvshowlist.data.entities.search.SearchApiResponse
import com.example.tvshowlist.data.remote.RetrofitInstance


class SearchTVShowsRepository {
    suspend fun getTVShows(query: String = ""): SearchApiResponse =
        RetrofitInstance.api.searchTvShowsByName(query = query)

    suspend fun getTVShowById(tvShowId: Int): GetTvShowApiResponse =
        RetrofitInstance.api.getTvShowById(id = tvShowId)
}