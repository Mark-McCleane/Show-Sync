package com.example.tvshowlist.ui.items

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.tvshowlist.domain.model.TvShowSeason
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@Composable
fun ItemTvShowChecker(tvShowSeason: TvShowSeason) {
    var isOverviewExpanded by remember { mutableStateOf(false) }
    var isWatched by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable { isWatched = changeWatchedStatus(isWatched) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = tvShowSeason.episodeImage,
                error = painterResource(id = android.R.drawable.stat_notify_error),
                contentDescription = "${tvShowSeason.episodeName} Episode Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)

            )
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    text = "${tvShowSeason.episodeNumber}. ${tvShowSeason.episodeName}",
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(modifier = Modifier.wrapContentSize()) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = androidx.compose.ui.graphics.Color.Yellow,
                        modifier = Modifier.weight(0.20f)
                    )
                    val rating =
                        String.format(Locale.getDefault(), "%.1f", tvShowSeason.voteAverage)
                    Text(
                        text = rating,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(0.25f)
                    )
                    val airDate = formatDate(tvShowSeason.episodeAirDate)
                    Text(
                        text = airDate,
                        maxLines = 1,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    )
                }
                Divider(thickness = 2.dp)

                Text(
                    text = tvShowSeason.overview,
                    modifier = Modifier.clickable { isOverviewExpanded = !isOverviewExpanded },
                    maxLines = if (isOverviewExpanded) Int.MAX_VALUE else 4
                )
            }
            Column(
                Modifier
                    .weight(weight = 0.35f)
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.End
            ) {
                Checkbox(
                    checked = isWatched,
                    onCheckedChange = { isWatched = changeWatchedStatus(isWatched) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

private fun changeWatchedStatus(isWatched: Boolean): Boolean {
    return !isWatched
}

fun formatDate(episodeAirDate: String): String {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return episodeAirDate

    val inputFormatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd",
        Locale.getDefault()
    )
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    return try {
        val date = LocalDate.parse(episodeAirDate, inputFormatter)
        date.format(outputFormatter)
    } catch (e: DateTimeParseException) {
        episodeAirDate
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemTvShowChecker() {
    val tvShowSeason = TvShowSeason(
        episodeId = 1,
        seasonNumber = 1,
        overview = "This is an overview",
        episodeName = "Episode Name",
        episodeImage = "https://api.themoviedb.org/3/8BUXYeIeRajrgfJawYXCPAMwzkw.jpg",
        episodeAirDate = "12/05/2024",
        episodeNumber = 1,
        voteAverage = 1.555555
    )
    ItemTvShowChecker(tvShowSeason)
}