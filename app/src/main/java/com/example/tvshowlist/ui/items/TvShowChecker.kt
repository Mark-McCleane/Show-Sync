package com.example.tvshowlist.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.tvshowlist.domain.model.TvShowSeason
import java.util.Locale

@Composable
fun ItemTvShowChecker(tvShowSeason: TvShowSeason) {
    var isOverviewExpanded by remember { mutableStateOf(false) }
    Card(modifier = Modifier.padding(4.dp)) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = tvShowSeason.episodeImage,
                error = painterResource(id = android.R.drawable.stat_notify_error),
                contentDescription = "${tvShowSeason.episodeName} Episode Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .sizeIn(
                        minWidth = 150.dp,
                        minHeight = 150.dp,
                        maxWidth = 170.dp,
                        maxHeight = 170.dp
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
            ) {
                Text(
                    text = "${tvShowSeason.episodeNumber}.\t${tvShowSeason.episodeName}",
                )

                Row(modifier = Modifier.wrapContentSize()) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star Icon",
                        tint = androidx.compose.ui.graphics.Color.Yellow
                    )
                    val rating =
                        String.format(Locale.getDefault(), "%.1f", tvShowSeason.voteAverage)
                    Text(
                        text = "${rating}\t\t",
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = tvShowSeason.episodeAirDate,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Divider(thickness = 2.dp)

                Text(
                    text = tvShowSeason.overview,
                    modifier = Modifier.clickable { isOverviewExpanded = !isOverviewExpanded },
                    maxLines = if (isOverviewExpanded) Int.MAX_VALUE else 4
                )
            }
        }
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