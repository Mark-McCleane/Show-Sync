package com.example.tvshowlist.data.remote

import com.example.tvshowlist.data.entities.getTvShow.GetTvShowApiResponse
import com.example.tvshowlist.data.entities.getTvShowSeason.GetSeasonApiResponse
import com.example.tvshowlist.data.entities.search.Result
import com.example.tvshowlist.domain.model.TvShowSeason
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.model.TvShowExtended

class AppMapper {
    companion object {
        fun mapGetTvShowsApiResultToTvShowList(apiResult: List<Result>): List<TvShow> {
            val tvShowList = mutableListOf<TvShow>()

            apiResult.forEach {
                tvShowList.add(
                    TvShow(
                        id = it.id,
                        title = it.name ?: "",
                        description = it.overview ?: "",
                        posterPath = it.poster_path ?: "",
                        airDate = it.first_air_date ?: ""
                    )
                )
            }

            return tvShowList
        }

        fun mapGetTvShowByIdApiResultToTvShow(apiResult: GetTvShowApiResponse): TvShowExtended {
            return TvShowExtended(
                tvShowId = apiResult.id,
                title = apiResult.name,
                seasonCount = apiResult.number_of_seasons
            )
        }

        fun mapGetTvShowSeasonsApiResultToTvShowSeason(apiResponse: GetSeasonApiResponse): List<TvShowSeason> {
            val listOfEpisodes = mutableListOf<TvShowSeason>()
            apiResponse.episodes.forEach {
                listOfEpisodes.add(
                    TvShowSeason(
                        episodeId = it.id,
                        episodeName = it.name,
                        episodeNumber = it.episode_number,
                        episodeAirDate = it.air_date,
                        overview = it.overview,
                        voteAverage = it.vote_average,
                        episodeImage = RetrofitInterface.IMAGE_BASE_URL + it.still_path,
                        seasonNumber = it.season_number,
                    )
                )
            }

            return listOfEpisodes
        }
    }
}