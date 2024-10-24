package com.example.tvshowlist.data.entities

import com.example.tvshowlist.data.entities.getTvShow.GetTvShowApiResponse
import com.example.tvshowlist.data.entities.search.Result
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
    }
}