package com.example.tvshowlist.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.tvshowlist.presentation.ui.HomeScreen
import com.example.tvshowlist.presentation.ui.EpisodeCheckerScreen.TvShowEpisodeChecker
import com.example.tvshowlist.presentation.ui.theme.TvShowListTheme
import com.example.tvshowlist.utils.ApplicationOnlineChecker
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TvShowListTheme {
                val viewModel: MainViewModel = koinViewModel()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = SearchScreenRoute) {
                    composable<SearchScreenRoute> {
                        HomeScreen(
                            viewModel = koinViewModel(),
                            navigateTo = {
                                it.addedToRecentDate = System.currentTimeMillis()
                                viewModel.insertRecentTvShow(it)
                                navController.navigate(TvShowCheckerScreenRoute(it.id, it.title))
                            }
                        )
                    }

                    composable<TvShowCheckerScreenRoute> { backStackEntry ->
                        val args = backStackEntry.toRoute<TvShowCheckerScreenRoute>()
                        val state by viewModel.episodeCheckerUIState.collectAsState()
                        val context = LocalContext.current

                        val tvShowId = args.tvShowId
                        val hasLoadedData = remember(tvShowId) { mutableStateOf(false) }

                        LaunchedEffect(Unit) {
                            if (!hasLoadedData.value && ApplicationOnlineChecker.isOnline(context)) {
                                viewModel.getTvShowById(tvShowId)
                                hasLoadedData.value = true
                            }
                        }

                        TvShowEpisodeChecker(
                            tvShowId = tvShowId,
                            tvShowName = args.tvShowName,
                            tvShow = state.tvShow,
                            seasonEpisodes = state.seasonEpisodes,
                            top10Episodes = state.top10Episodes,
                            isEpisodesLoaded = state.isEpisodesLoaded,
                            error = state.error,
                            onSeasonSelected = { seasonNumber ->
                                if (ApplicationOnlineChecker.isOnline(context)) {
                                    viewModel.getTvShowSeasons(tvShowId, seasonNumber)
                                } else {
                                    viewModel.getTvShowSeasonsOffline(tvShowId, seasonNumber)
                                }
                                viewModel.getTop10TvShowEpisodesById(tvShowId)
                            },
                            onEpisodeWatchedToggle = { episodeId, isWatched ->
                                viewModel.updateIsWatchedState(episodeId, isWatched)
                            },
                            onCheckAllEpisodes = { seasonEpisodes, isChecked ->
                                seasonEpisodes.forEach { episode ->
                                    viewModel.updateIsWatchedState(episode.episodeId, isChecked)
                                }
                            }

                        )
                    }
                }
            }
        }
    }
}

@Serializable
object SearchScreenRoute

@Serializable
data class TvShowCheckerScreenRoute(val tvShowId: Int, val tvShowName: String)