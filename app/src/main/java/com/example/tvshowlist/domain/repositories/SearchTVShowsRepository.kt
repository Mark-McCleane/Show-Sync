package com.example.tvshowlist.domain.repositories

import com.example.tvshowlist.data.entities.SearchApiResponse
import com.example.tvshowlist.data.remote.RetrofitInstance
import com.example.tvshowlist.domain.model.TvShow


class SearchTVShowsRepository {
    suspend fun getTVShows(query: String = ""): SearchApiResponse =
        RetrofitInstance.api.searchTvShowsByName(query = query)
}