package com.example.tvshowlist.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tvshowlist.MainViewModel
import com.example.tvshowlist.R
import com.example.tvshowlist.domain.repositories.SearchTVShowsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowEpisodeChecker(tvShowId: Int, viewModel: MainViewModel) {
    viewModel.getTvShowById(tvShowId)
    viewModel.getTvShowSeasons(tvShowId)
    val tvShow = viewModel.selectedTvShow.collectAsState()
    val seasonEpisodes = viewModel.selectedSeason.collectAsState()
    val context = LocalContext.current
    var isSeasonsDropDownExpanded by remember {
        mutableStateOf(false)
    }
    var seasonSelectedIndex by remember {
        mutableIntStateOf(0)
    }

    val seasonList = (1..(tvShow.value?.seasonCount ?: 1)).toList()


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = tvShow.value?.title ?: ""
            )
        })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .clickable(onClick = { isSeasonsDropDownExpanded = true }),
        ) {
            Text(
                text = "Seasons",
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            )

            Image(
                painter = painterResource(id = R.drawable.tvseries_seasons_drop_down_24),
                contentDescription = "Tv Series Seasons Dropdown",
                modifier = Modifier.align(Alignment.CenterEnd),
            )

            DropdownMenu(
                expanded = isSeasonsDropDownExpanded,
                onDismissRequest = { isSeasonsDropDownExpanded = false },
                modifier = Modifier.wrapContentSize()
            ) {
                seasonList.forEachIndexed { index, seasonNumber ->
                    DropdownMenuItem(
                        text = {
                            Text(text = "Season $seasonNumber")
                        },
                        onClick = {
                            seasonSelectedIndex = index
                            isSeasonsDropDownExpanded = false
                            Toast.makeText(
                                context,
                                "Season $seasonNumber Selected",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
                //use https://developer.themoviedb.org/reference/tv-season-details
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            itemsIndexed(seasonEpisodes.value) { index, seasonEpisode ->
                Text(text = "Episode ${seasonEpisode.episodeName} which aired in ${seasonEpisode.episodeAirDate}!\nOverview\t${seasonEpisode.overview}")
            }
        }
    }
}

@Preview
@Composable
fun TvShowEpisodeCheckerPreview() {
    TvShowEpisodeChecker(1, MainViewModel(SearchTVShowsRepository()))
}