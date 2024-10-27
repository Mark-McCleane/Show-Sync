package com.example.tvshowlist.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowCheckerDao {
    @Upsert()
    suspend fun upsertTvShowChecker(tvShowChecker: TvShowSeasonEpisodes)

    @Query("SELECT * FROM tvShowSeasonEpisodesTable t")
    suspend fun getAllTvShowsSeasonEpisodes(): List<TvShowSeasonEpisodes>

    @Query("UPDATE tvShowSeasonEpisodesTable set isChecked = :isWatchedStatus WHERE episodeId = :episodeId")
    suspend fun updateIsWatchedStatus(episodeId: Int, isWatchedStatus: Boolean)

}