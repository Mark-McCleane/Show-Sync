package com.example.tvshowlist.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.tvshowlist.di.AppModules
import com.example.tvshowlist.presentation.ui.HomeScreen
import com.example.tvshowlist.presentation.ui.TvShowEpisodeChecker
import com.example.tvshowlist.presentation.ui.theme.TvShowListTheme
import kotlinx.serialization.Serializable
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        startKoin {
            androidContext(this@MainActivity)
            modules(AppModules.mainModule, AppModules.databaseModule)
        }

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
                        TvShowEpisodeChecker(
                            tvShowId = args.tvShowId,
                            tvShowName = args.tvShowName,
                            viewModel = koinViewModel(),
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