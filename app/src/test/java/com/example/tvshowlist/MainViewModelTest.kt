package com.example.tvshowlist

import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.repositories.TvShowsRepository
import com.example.tvshowlist.presentation.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@ExperimentalCoroutinesApi
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testModules = module {
        single<TvShowsRepository> { mockk<TvShowsRepository>() }
        viewModel { MainViewModel(get()) }
    }

    private val tvShow1 = TvShow(
        id = 1,
        description = "Show 1",
        title = "Description 1",
        airDate = "https://example.com/show1.jpg"
    )
    private val tvShow2 = TvShow(
        id = 2,
        description = "Show 2",
        title = "Description 2",
        airDate = "https://example.com/show2.jpg"
    )

    @ExperimentalCoroutinesApi
    class MainDispatcherRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
        TestWatcher() {
        override fun starting(description: Description?) = Dispatchers.setMain(dispatcher)

        override fun finished(description: Description?) = Dispatchers.resetMain()

    }

    @Before
    fun setUp() {
        startKoin { modules(testModules) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `tests getRecentTvShows() returns list of recent tv shows`() = runTest {
        val repository: TvShowsRepository = mockk()

        coEvery { repository.getRecentTvShows() } returns flowOf(
            listOf(
                tvShow1,
                tvShow2,
                tvShow1,
                tvShow2
            )
        )
        viewModel = MainViewModel(repository)
        viewModel.getRecentTvShows()

        val expectedList = listOf(tvShow1, tvShow2)
        val actualTvShows = viewModel.recentTvShowList.value
        assertEquals(expectedList, actualTvShows)
    }
}