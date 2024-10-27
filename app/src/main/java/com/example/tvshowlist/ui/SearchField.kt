package com.example.tvshowlist.ui

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tvshowlist.MainViewModel
import com.example.tvshowlist.data.db.TvShowCheckerDao
import com.example.tvshowlist.data.remote.RetrofitInterface
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.repositories.SearchTVShowsRepository
import com.example.tvshowlist.ui.items.ItemTvShow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    viewModel: MainViewModel,
    navigateTo: (tvShow: TvShow) -> Unit
) {
    val searchText by viewModel.searchText.collectAsState()
    val tvShowList by viewModel.tvShowList.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text(text = "TV Show App") }) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(value = searchText,
                onValueChange = viewModel::onSearchTextChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Search Tv Show") },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Tv Shows"
                        )
                    }
                })

            Spacer(modifier = Modifier.height(16.dp))

            if (isSearching) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                if (tvShowList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (searchText.isNotEmpty()) "No Tv Shows Found" else "Please Search For A Tv Show",
                            style = TextStyle(fontSize = 50.sp, fontWeight = FontWeight.ExtraBold),
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        items(tvShowList) { tvShow ->
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