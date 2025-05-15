package com.example.tvshowlist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes

@Dao
interface TvShowCheckerDao {
    @Upsert
    suspend fun insertRecentTvShow(tvShow: TvShow)

    @Delete
    suspend fun deleteRecentTvShow(tvShow: TvShow)

    @Query("SELECT * FROM tvShowTable t ORDER BY t.addedToRecentDate DESC LIMIT 20")
    suspend fun getRecentTvShows(): List<TvShow>

    @Upsert()
    suspend fun upsertTvShowChecker(tvShowChecker: TvShowSeasonEpisodes)

    @Query("UPDATE tvShowSeasonEpisodesTable SET isChecked = :isWatchedStatus WHERE episodeId = :episodeId")
    suspend fun updateIsWatchedStatus(episodeId: Int, isWatchedStatus: Boolean)

    @Query("SELECT isChecked FROM tvShowSeasonEpisodesTable WHERE episodeId = :episodeId")
    suspend fun getIsWatchedStatus(episodeId: Int): Boolean

    @Query("SELECT * FROM tvShowSeasonEpisodesTable WHERE tvShowId = :tvShowId AND seasonNumber = :seasonSelected ORDER BY episodeNumber ASC")
    suspend fun getTvShowSeasonOffline(tvShowId: Int, seasonSelected: Int) : List<TvShowSeasonEpisodes>

    @Query("SELECT * FROM tvShowSeasonEpisodesTable WHERE tvShowId = :tvShowId ORDER BY voteAverage  Desc LIMIT 10")
    suspend fun getTop10TvShowById(tvShowId: Int): List<TvShowSeasonEpisodes>
}