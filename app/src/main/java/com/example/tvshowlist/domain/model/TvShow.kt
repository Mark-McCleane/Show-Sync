package com.example.tvshowlist.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity(tableName = "tvShowTable")
data class TvShow(
    @PrimaryKey val id: Int,
    val description: String? = "",
    val title: String = "",
    val airDate: String? = "",
    val posterPath: String? = "",
    var addedToRecentDate: Long? = null,
) : Parcelable {
    fun searchTvShow(query: String): Boolean {
        val matchingCombination = listOf(title) + title.trim().split(" ")

        return matchingCombination.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
