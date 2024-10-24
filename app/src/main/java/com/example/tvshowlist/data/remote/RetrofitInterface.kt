package com.example.tvshowlist.data.remote

import com.example.tvshowlist.data.entities.getTvShow.GetTvShowApiResponse
import com.example.tvshowlist.data.entities.getTvShowSeason.GetSeasonApiResponse
import com.example.tvshowlist.data.entities.search.SearchApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {
//    https://api.themoviedb.org/3/search/tv?query=test&include_adult=false&language=en-US&page=1

    @GET("search/tv")
    suspend fun searchTvShowsByName(
        @Query("query") query: String = "",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): SearchApiResponse

    //'https://api.themoviedb.org/3/tv/1402?language=en-US'
    @GET("tv/{id}")
    suspend fun getTvShowById(
        @Path("id") id: Int,
        @Query("language") language: String = "en-US"
    ): GetTvShowApiResponse

    //    https://api.themoviedb.org/3/tv/1402/season/1?language=en-US
    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getSeasonById(
        @Path("tv_id") tvId: Int,
        @Path("season_number") seasonNumber: Int = 1,
        @Query("language") language: String = "en-US"
    ): GetSeasonApiResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"
    }
}