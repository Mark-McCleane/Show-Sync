package com.example.tvshowlist.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.tvshowlist.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowEpisodeChecker(tvShowId: Int,viewModel: MainViewModel) {
    viewModel.getTvShowById(tvShowId)
    val tvShow = viewModel.selectedTvShow.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = tvShow.value?.title ?: ""
            )
        })
    }) { innerPadding ->
        Text(
            text = "Season Count: ${tvShow.value?.seasonCount ?: 0}",
            modifier = Modifier.padding(innerPadding)
        )
    }
}