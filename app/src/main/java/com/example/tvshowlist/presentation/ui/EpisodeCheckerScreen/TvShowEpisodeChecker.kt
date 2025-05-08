package com.example.tvshowlist.presentation.ui.EpisodeCheckerScreen

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.tvshowlist.R
import com.example.tvshowlist.domain.model.TvShowExtended
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes
import com.example.tvshowlist.utils.ApplicationOnlineChecker
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowEpisodeChecker(
    tvShowId: Int,
    tvShowName: String,
    tvShow: TvShowExtended?,
    seasonEpisodes: List<TvShowSeasonEpisodes>,
    top10Episodes: List<TvShowSeasonEpisodes>,
    isEpisodesLoaded: Boolean,
    error: String,
    checkedButton: Boolean,
    isCensored: Boolean,
    formatAirDate: (String) -> String,
    navigateBack: () -> Unit,
    onSeasonSelected: (Int) -> Unit,
    onEpisodeWatchedToggle: (Int, Boolean, Int) -> Unit,
    onCheckAllEpisodes: (List<TvShowSeasonEpisodes>, Boolean, Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var isSeasonsDropDownExpanded by remember {
        mutableStateOf(false)
    }

    var seasonSelected by remember {
        mutableIntStateOf(1)
    }

    val currentSeasonEpisodes = if (seasonSelected > 0) {
        seasonEpisodes.filter { it.seasonNumber == seasonSelected }
    } else {
        top10Episodes
    }

    var checkAll by remember {
        mutableStateOf(checkedButton)
    }

    LaunchedEffect(key1 = seasonSelected) {
        onSeasonSelected(seasonSelected)
    }

    LaunchedEffect(key1 = error) {
        if (error.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                if (!ApplicationOnlineChecker.isOnline(context)) context.getString(R.string.no_internet_connection) else error,
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    val seasonList = (1..(tvShow?.seasonCount ?: 1)).toList()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = tvShow?.title ?: tvShowName
            )
        }, navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.dropdown_arrow)
                )
            }
        })
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { innerPadding ->
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
                        text = if (seasonSelected > 0) stringResource(
                            R.string.season_selected_number,
                            seasonSelected
                        ) else stringResource(
                            R.string.top_episodes
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = stringResource(R.string.dropdown_arrow),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f)
                    )
                }

                DropdownMenu(
                    expanded = isSeasonsDropDownExpanded,
                    onDismissRequest = { isSeasonsDropDownExpanded = false },
                    modifier = Modifier.width(IntrinsicSize.Min)
                ) {
                    DropdownMenuItem(onClick = {
                        seasonSelected = -1
                        isSeasonsDropDownExpanded = false
                    }, text = {
                        Text(
                            text = stringResource(R.string.top_episodes),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    })
                    seasonList.forEachIndexed { index, seasonNumber ->
                        DropdownMenuItem(onClick = {
                            seasonSelected = seasonNumber
                            isSeasonsDropDownExpanded = false
                        }, text = {
                            Text(
                                text = stringResource(R.string.season_no, seasonNumber),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        })
                    }
                }
            }
            if (isEpisodesLoaded) {
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
                            text = stringResource(R.string.no_episodes_found_for_this_season),
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                } else if (currentSeasonEpisodes.size >= 2) {
                    Column(
                        modifier = Modifier
                            .clickable {
                                onCheckAllEpisodes(currentSeasonEpisodes, !checkAll, seasonSelected)
                                checkAll = !checkAll
                            }
                            .fillMaxWidth()
                            .padding(0.dp)
                    ) {
                        Divider()
                        ListItem(modifier = Modifier, headlineContent = {
                            Text(stringResource(R.string.check_all), modifier = Modifier)
                        })
                        Divider()
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 0.dp, top = 0.dp, bottom = 16.dp, end = 0.dp),
                    state = rememberLazyListState(),
                ) {
                    itemsIndexed(
                        items = currentSeasonEpisodes,
                        key = { _, episode -> episode.episodeId }
                    ) { index, seasonEpisode ->
                        var isExpanded by rememberSaveable(seasonEpisode.episodeId) {
                            mutableStateOf(
                                false
                            )
                        }
                        var isTextEnabled by rememberSaveable(seasonEpisode.episodeId) {
                            mutableStateOf(
                                index == 0 || currentSeasonEpisodes[index - 1].isChecked || isExpanded
                            )
                        }

                        ListItem(
                            headlineContent = {
                                val seasonEpisodeText =
                                    if (seasonSelected > 0) "${seasonEpisode.episodeNumber}. ${seasonEpisode.episodeName}" else "${index + 1}. (${seasonEpisode.seasonNumber}X${seasonEpisode.episodeNumber}) ${seasonEpisode.episodeName}"
                                Text(
                                    text = seasonEpisodeText,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                                RatingSection(tvShowSeasonEpisodes = seasonEpisode, formatAirDate(seasonEpisode.episodeAirDate))
                            }, supportingContent = {
                                Text(
                                    text = seasonEpisode.overview,
                                    modifier = if (isCensored && !isTextEnabled && index != 0 && !currentSeasonEpisodes[index - 1].isChecked)
                                        Modifier
                                            .clickable {
                                                isExpanded = !isExpanded
                                                isTextEnabled = !isTextEnabled
                                            }
                                            .blur(15.dp)
                                    else Modifier.clickable {
                                        isExpanded = !isExpanded
                                        isTextEnabled = !isTextEnabled
                                    },
                                    maxLines = if (isExpanded) Int.MAX_VALUE else 3
                                )
                            }, overlineContent = {},
                            leadingContent = {
                                val manuallyRevealed =
                                    rememberSaveable(seasonEpisode.episodeId) { mutableStateOf(false) }

                                val blurValue =
                                    if (manuallyRevealed.value || index == 0 || currentSeasonEpisodes[index - 1].isChecked || !isCensored) {
                                        0.dp
                                    } else {
                                        15.dp
                                    }

                                AsyncImage(
                                    model = seasonEpisode.episodeImage,
                                    error = painterResource(id = android.R.drawable.stat_notify_error),
                                    contentDescription = stringResource(
                                        R.string.episode_image,
                                        seasonEpisode.episodeName
                                    ),
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clickable {
                                            manuallyRevealed.value = !manuallyRevealed.value
                                        }
                                        .blur(blurValue)
                                )
                            }, trailingContent = {
                                Checkbox(
                                    checked = seasonEpisode.isChecked,
                                    onCheckedChange = null,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }, modifier = Modifier.clickable {
                                val newWatchedState = !(seasonEpisode.isChecked)
                                onEpisodeWatchedToggle(
                                    seasonEpisode.episodeId,
                                    newWatchedState,
                                    seasonSelected
                                )
                            })
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingSection(tvShowSeasonEpisodes: TvShowSeasonEpisodes, airDate: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = stringResource(R.string.star_icon),
            tint = androidx.compose.ui.graphics.Color.Yellow,
            modifier = Modifier.weight(0.20f)
        )
        val rating = String.format(Locale.getDefault(), "%.1f", tvShowSeasonEpisodes.voteAverage)

        Text(
            text = rating,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.25f)
        )
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