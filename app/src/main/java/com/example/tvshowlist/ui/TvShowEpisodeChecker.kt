package com.example.tvshowlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tvshowlist.MainViewModel
import com.example.tvshowlist.R
import com.example.tvshowlist.ui.items.ItemTvShowChecker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowEpisodeChecker(tvShowId: Int, viewModel: MainViewModel) {
    val tvShow = viewModel.selectedTvShow.collectAsState()
    val seasonEpisodes = viewModel.selectedSeason.collectAsState()
    val isEpisodesLoaded by viewModel.isEpisodesLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val error by viewModel.error.collectAsState()
    var isSeasonsDropDownExpanded by remember {
        mutableStateOf(false)
    }
    var seasonSelected by remember {
        mutableIntStateOf(1)
    }

    LaunchedEffect(key1 = tvShow) {
        viewModel.getTvShowById(tvShowId)
    }

    LaunchedEffect(key1 = seasonSelected) {
        viewModel.getTvShowSeasons(tvShowId, seasonSelected)
    }

    LaunchedEffect(key1 = error) {
        if(error.isNotEmpty()){
            snackbarHostState.showSnackbar(error, withDismissAction = true, duration = SnackbarDuration.Indefinite)
        }
    }

    val seasonList = (1..(tvShow.value?.seasonCount ?: 1)).toList()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = tvShow.value?.title ?: ""
            )
        })
    },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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
                        .align(Alignment.Center)
                        .wrapContentSize()
                )

                Image(
                    painter = painterResource(id = R.drawable.tvseries_seasons_drop_down_24),
                    contentDescription = "Tv Series Seasons Dropdown",
                    modifier = Modifier.align(Alignment.Center)

                )

                DropdownMenu(
                    expanded = isSeasonsDropDownExpanded,
                    onDismissRequest = { isSeasonsDropDownExpanded = false },
                    modifier = Modifier.wrapContentSize()
                ) {
                    seasonList.forEachIndexed { index, seasonNumber ->
                        DropdownMenuItem(text = {
                            Text(
                                text = "Season $seasonNumber",
                                textAlign = TextAlign.Center
                            )
                        }, onClick = {
                            seasonSelected = seasonNumber
                            isSeasonsDropDownExpanded = false
                        })
                    }
                }
            }
            if (isEpisodesLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 0.dp, top = 0.dp, bottom = 16.dp, end = 0.dp)
                ) {
                    itemsIndexed(seasonEpisodes.value.filter { it.seasonNumber == seasonSelected }) { index, seasonEpisode ->
                        ItemTvShowChecker(
                            tvShowSeasonEpisodes = seasonEpisode,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}