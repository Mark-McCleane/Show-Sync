package com.example.tvshowlist.data.remote

data class TvShowSeason(
    val episodeId: Int,
    val episodeName: String,
    val episodeAirDate: String,
    val voteAverage: Double,
    val overview: String,
)
