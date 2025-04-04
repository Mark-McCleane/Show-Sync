package com.example.tvshowlist.presentation.ui

import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.tvshowlist.R
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes
import com.example.tvshowlist.presentation.MainViewModel
import com.example.tvshowlist.utils.ApplicationOnlineChecker
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowEpisodeChecker(
    tvShowId: Int,
    tvShowName: String,
    viewModel: MainViewModel
) {
    val state by viewModel.episodeCheckerUIState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var isSeasonsDropDownExpanded by remember {
        mutableStateOf(false)
    }
    var seasonSelected by remember {
        mutableIntStateOf(1)
    }

    val currentSeasonEpisodes = state.seasonEpisodes.filter { it.seasonNumber == seasonSelected }

    val allChecked = currentSeasonEpisodes.isNotEmpty() &&
            currentSeasonEpisodes.all { it.isChecked == true }


    var checkedAll by remember(seasonSelected) {
        mutableStateOf(allChecked)
    }

    LaunchedEffect(currentSeasonEpisodes.map { it.isChecked }) {
        checkedAll = currentSeasonEpisodes.isNotEmpty() &&
                currentSeasonEpisodes.all { it.isChecked == true }
    }

    LaunchedEffect(key1 = state.tvShow) {
        if (ApplicationOnlineChecker.isOnline(context)) {
            viewModel.getTvShowById(tvShowId)
        }
    }

    LaunchedEffect(key1 = seasonSelected) {
        if (ApplicationOnlineChecker.isOnline(context)) {
            viewModel.getTvShowSeasons(tvShowId, seasonSelected)
        } else {
            viewModel.getTvShowSeasonsOffline(tvShowId, seasonSelected)
        }
    }

    LaunchedEffect(key1 = state.error) {
        if (state.error.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                if (!ApplicationOnlineChecker.isOnline(context)) "No Internet Connection" else state.error,
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    val seasonList = (1..(state.tvShow?.seasonCount ?: 1)).toList()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = state.tvShow?.title ?: tvShowName
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
                    .clickable { isSeasonsDropDownExpanded = true }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Season $seasonSelected",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown Arrow",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f)
                    )
                }

                DropdownMenu(
                    expanded = isSeasonsDropDownExpanded,
                    onDismissRequest = { isSeasonsDropDownExpanded = false },
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                ) {
                    seasonList.forEachIndexed { index, seasonNumber ->
                        DropdownMenuItem(
                            onClick = {
                                seasonSelected = seasonNumber
                                isSeasonsDropDownExpanded = false
                            },
                            text = {
                                Text(
                                    text = "Season $seasonNumber",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                }
            }
            if (state.isEpisodesLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                if (currentSeasonEpisodes.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 32.dp, top = 16.dp, bottom = 16.dp, end = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "No episodes found for this season",
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                } else if (currentSeasonEpisodes.size >= 2) {
                    Column(
                        modifier = Modifier
                            .clickable {
                                val newChecked = !checkedAll
                                currentSeasonEpisodes.forEach { episode ->
                                    viewModel.updateIsWatchedState(
                                        episodeId = episode.episodeId,
                                        isWatched = newChecked
                                    )
                                }
                            }
                            .fillMaxWidth()
                            .padding(0.dp)
                    ) {
                        Divider()
                        ListItem(
                            modifier = Modifier,
                            headlineContent = {
                                Text(stringResource(R.string.check_all), modifier = Modifier)
                            },
                            trailingContent = {
                                Checkbox(
                                    checked = checkedAll,
                                    modifier = Modifier,
                                    onCheckedChange = null
                                )
                            }
                        )
                        Divider()
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 0.dp, top = 0.dp, bottom = 16.dp, end = 0.dp)
                ) {
                    itemsIndexed(currentSeasonEpisodes) { index, seasonEpisode ->
                        var isExpanded by remember { mutableStateOf(false) }
                        val isWatched = seasonEpisode.isChecked ?: false

                        ListItem(
                            headlineContent = {
                                Text(
                                    text = "${seasonEpisode.episodeNumber}. ${seasonEpisode.episodeName}",
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                                RatingSection(tvShowSeasonEpisodes = seasonEpisode)
                            },
                            supportingContent = {
                                Text(
                                    text = seasonEpisode.overview,
                                    modifier = Modifier.clickable { isExpanded = !isExpanded },
                                    maxLines = if (isExpanded) Int.MAX_VALUE else 3
                                )
                            },
                            overlineContent = {
                            },

                            leadingContent = {
                                AsyncImage(
                                    model = seasonEpisode.episodeImage,
                                    error = painterResource(id = android.R.drawable.stat_notify_error),
                                    contentDescription = "${seasonEpisode.episodeName} Episode Image",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(100.dp)
                                )
                            },
                            trailingContent = {
                                Checkbox(
                                    checked = isWatched,
                                    onCheckedChange = null,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            },
                            modifier = Modifier.clickable {
                                val newWatchedState = !isWatched
                                viewModel.updateIsWatchedState(
                                    episodeId = seasonEpisode.episodeId,
                                    isWatched = newWatchedState
                                )
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingSection(tvShowSeasonEpisodes: TvShowSeasonEpisodes) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Star Icon",
            tint = androidx.compose.ui.graphics.Color.Yellow,
            modifier = Modifier.weight(0.20f)
        )
        val rating =
            String.format(Locale.getDefault(), "%.1f", tvShowSeasonEpisodes.voteAverage)

        Text(
            text = rating,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.25f)
        )
        val airDate = formatDate(tvShowSeasonEpisodes.episodeAirDate)
        Text(
            text = airDate,
            maxLines = 1,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
    }
}

private fun formatDate(episodeAirDate: String): String {
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