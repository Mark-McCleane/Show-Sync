package com.example.tvshowlist.domain.model

data class TvShowSeason(
    val episodeId: Int,
    val episodeName: String,
    val episodeNumber: Int,
    val episodeAirDate: String,
    val episodeImage: String,
    val seasonNumber: Int,
    val voteAverage: Double = 0.0,
    val overview: String,
    val isChecked: Boolean = false,
)
