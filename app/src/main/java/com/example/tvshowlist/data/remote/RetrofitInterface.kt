package com.example.tvshowlist.data.remote

import com.example.tvshowlist.data.entities.SearchApiResponse
import com.example.tvshowlist.domain.model.TvShow
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitInterface {
//    https://api.themoviedb.org/3/search/tv?query=test&include_adult=false&language=en-US&page=1

    @GET("search/tv")
    suspend fun searchTvShowsByName(
        @Query("query") query: String = "",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ) : SearchApiResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"
    }
}