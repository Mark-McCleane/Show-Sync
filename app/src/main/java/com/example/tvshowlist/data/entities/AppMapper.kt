package com.example.tvshowlist.data.entities

import com.example.tvshowlist.domain.model.TvShow

class AppMapper {
    companion object {
        fun mapApiResultToTvShow(apiResult: List<Result>): List<TvShow> {
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


    }
}