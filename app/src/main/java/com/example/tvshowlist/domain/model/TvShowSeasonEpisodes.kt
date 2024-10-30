package com.example.tvshowlist.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tvShowSeasonEpisodesTable")
data class TvShowSeasonEpisodes(
    @PrimaryKey val episodeId: Int,
    val episodeName: String,
    val episodeNumber: Int,
    val episodeAirDate: String,
    val episodeImage: String,
    val seasonNumber: Int,
    val voteAverage: Double = 0.0,
    val overview: String,
    var tvShowId: Int? = null,
    var isChecked: Boolean? = null,
)
