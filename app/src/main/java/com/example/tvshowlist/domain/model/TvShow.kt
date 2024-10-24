package com.example.tvshowlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TvShow(
    val id: Int,
    val description: String = "",
    val title: String = "",
    val airDate: String = "",
    val posterPath: String = ""
) : Parcelable {
    fun searchTvShow(query: String): Boolean {
        val matchingCombination = listOf(title) + title.trim().split(" ")

        return matchingCombination.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
