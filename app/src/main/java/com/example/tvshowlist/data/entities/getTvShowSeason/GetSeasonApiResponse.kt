package com.example.tvshowlist.data.entities.getTvShowSeason

import com.google.gson.annotations.SerializedName

data class GetSeasonApiResponse(
    val _id: String,
    val air_date: String,
    val episodes: List<Episode>,
    @SerializedName("id") val seasonId: Int,
    val name: String,
    val overview: String,
    val poster_path: String,
    val season_number: Int,
    val vote_average: Double
)