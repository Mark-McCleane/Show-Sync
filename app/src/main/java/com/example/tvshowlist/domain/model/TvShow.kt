package com.example.tvshowlist.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "tvShowTable")
data class TvShow(
    @PrimaryKey val id: Int,
    val description: String? = "",
    val title: String = "",
    val airDate: String? = "",
    val posterPath: String? = "",
    var addedToRecentDate: Long? = null,
){
    fun searchTvShow(query: String): Boolean {
        val matchingCombination = listOf(title) + title.trim().split(" ")

        return matchingCombination.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
