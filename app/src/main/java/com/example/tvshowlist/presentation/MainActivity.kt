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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.tvshowlist.presentation.ui.HomeScreen
import com.example.tvshowlist.presentation.ui.EpisodeCheckerScreen.TvShowEpisodeChecker
import com.example.tvshowlist.presentation.ui.SettingsScreen.SettingScreen
import com.example.tvshowlist.presentation.ui.SettingsScreen.SettingsViewModel
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
                val mainViewModel: MainViewModel = koinViewModel()
                val settingsViewModel: SettingsViewModel = koinViewModel()
                val navController = rememberNavController()
                val isPotentialSpoilerCensorshipEnabled by settingsViewModel.isPotentialSpoilerCensorshipEnabled.collectAsStateWithLifecycle()

                NavHost(navController = navController, startDestination = SearchScreenRoute) {
                    composable<SearchScreenRoute> {
                        HomeScreen(
                            viewModel = koinViewModel(),
                            navigateTo = {
                                it.addedToRecentDate = System.currentTimeMillis()
                                mainViewModel.insertRecentTvShow(it)
                                navController.navigate(TvShowCheckerScreenRoute(it.id, it.title))
                            },
                            navigateToSettings = {
                                navController.navigate(SettingScreenRoute)
                            }
                        )
                    }

                    composable<SettingScreenRoute> {
                        SettingScreen(
                            viewModel = koinViewModel(),
                            censorContent = isPotentialSpoilerCensorshipEnabled,
                            onCensorPotentialSpoilerContentChange = {
                                settingsViewModel.setPotentialSpoilerEnabled(it)
                            },
                            navigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable<TvShowCheckerScreenRoute> { backStackEntry ->
                        val args = backStackEntry.toRoute<TvShowCheckerScreenRoute>()
                        val state by mainViewModel.episodeCheckerUIState.collectAsState()
                        val context = LocalContext.current

                        val tvShowId = args.tvShowId
                        val hasLoadedData = remember(tvShowId) { mutableStateOf(false) }

                        LaunchedEffect(Unit) {
                            if (!hasLoadedData.value && ApplicationOnlineChecker.isOnline(context)) {
                                mainViewModel.getTvShowById(tvShowId)
                                hasLoadedData.value = true
                            }
                        }

                        TvShowEpisodeChecker(
                            tvShowId = tvShowId,
                            tvShowName = args.tvShowName,
                            tvShow = state.tvShow,
                            seasonEpisodes = state.seasonEpisodes,
                            checkedButton = state.checkedButton,
                            top10Episodes = state.top10Episodes,
                            isEpisodesLoaded = state.isEpisodesLoaded,
                            error = state.error,
                            isCensored = isPotentialSpoilerCensorshipEnabled,
                            navigateBack = {
                                navController.popBackStack()
                            },
                            formatAirDate = { date ->
                                mainViewModel.formatDate(date)
                            },
                            onSeasonSelected = { seasonNumber ->
                                if (ApplicationOnlineChecker.isOnline(context)) {
                                    mainViewModel.getTvShowSeasons(tvShowId, seasonNumber)
                                } else {
                                    mainViewModel.getTvShowSeasonsOffline(tvShowId, seasonNumber)
                                }
                                mainViewModel.getTop10TvShowEpisodesById(tvShowId)
                            },
                            onEpisodeWatchedToggle = { episodeId, isWatched, seasonNumber ->
                                mainViewModel.updateIsWatchedState(episodeId, isWatched, seasonNumber)
                            },
                            onCheckAllEpisodes = { seasonEpisodes, isChecked, seasonNumber ->
                                seasonEpisodes.forEach { episode ->
                                    mainViewModel.updateIsWatchedState(
                                        episode.episodeId,
                                        isChecked,
                                        seasonNumber
                                    )
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

@Serializable
object SettingScreenRoute