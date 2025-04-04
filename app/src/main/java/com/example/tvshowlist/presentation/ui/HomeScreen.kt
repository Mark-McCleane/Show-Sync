package com.example.tvshowlist.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tvshowlist.R
import com.example.tvshowlist.presentation.MainViewModel
import com.example.tvshowlist.data.remote.RetrofitInterface
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.presentation.ui.items.ItemTvShow
import com.example.tvshowlist.utils.ApplicationOnlineChecker
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = koinViewModel(),
    navigateTo: (tvShow: TvShow) -> Unit
) {
    val searchText by viewModel.searchText.collectAsState()
    val tvShowList by viewModel.tvShowList.collectAsState()
    val recentTvShowList by viewModel.recentTvShowList.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(recentTvShowList) {
        viewModel.getRecentTvShows()
    }

    LaunchedEffect(key1 = error) {
        if (error.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = if (!ApplicationOnlineChecker.isOnline(context)) "No Internet Connection" else error,
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "TV Show App") })
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = searchText,
                onValueChange = viewModel::onSearchTextChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = stringResource(R.string.search_tv_show)) },
                trailingIcon = {
                    if (searchText.isEmpty()) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search_tv_show)
                            )
                        }
                    } else {
                        IconButton(onClick = { viewModel.onSearchTextChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_search_tv_shows)
                            )
                        }
                    }
                })

            Spacer(modifier = Modifier.height(16.dp))

            if (isSearching || isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                if (searchText.isEmpty() && recentTvShowList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Please Search For A Tv Show",
                            style = TextStyle(fontSize = 50.sp, fontWeight = FontWeight.ExtraBold),
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        items(
                            if (searchText.isNotEmpty()) tvShowList else recentTvShowList
                        ) { tvShow ->
                            ItemTvShow(
                                tvShow = tvShow,
                                tvShowTitle = tvShow.title,
                                tvShowTagLine = tvShow.description ?: "No Description Found",
                                tvShowAirDate = tvShow.airDate ?: "N/A",
                                imageUrl = RetrofitInterface.IMAGE_BASE_URL + tvShow.posterPath,
                                modifier = Modifier.wrapContentSize(),
                                navigateTo = navigateTo
                            )
                        }
                    }
                }
            }
        }
    }
}