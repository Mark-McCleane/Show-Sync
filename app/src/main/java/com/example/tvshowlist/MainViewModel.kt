package com.example.tvshowlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshowlist.data.entities.search.Result
import com.example.tvshowlist.data.remote.AppMapper
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.model.TvShowExtended
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes
import com.example.tvshowlist.domain.repositories.SearchTVShowsRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MainViewModel(
    private val repository: SearchTVShowsRepository
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching
        .onStart { getTvShows() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isEpisodesLoading = MutableStateFlow(false)
    val isEpisodesLoading = _isEpisodesLoading.asStateFlow()

    private val _selectedTvShow = MutableStateFlow<TvShowExtended?>(null)
    val selectedTvShow = _selectedTvShow.asStateFlow()

    private val _selectedSeason = MutableStateFlow(listOf<TvShowSeasonEpisodes>())
    val selectedSeason = _selectedSeason.asStateFlow()

    private val _tvShowList = MutableStateFlow(listOf<TvShow>())
    val tvShowList = searchText
        .debounce(1_000L)
        .onEach { _isSearching.update { true } }
        .combine(_tvShowList) { text, tvShow ->
            if (text.isBlank()) {
                tvShow
            } else {
                tvShow.filter { it.searchTvShow(text) }
            }
        }.onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), _tvShowList.value
        )

    private val _recentTvShowList = MutableStateFlow(listOf<TvShow>())
    val recentTvShowList = _recentTvShowList.asStateFlow()

    init {
        viewModelScope.launch {
            getRecentTvShows()
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        viewModelScope.launch { getTvShows(text) }
    }

    private suspend fun getTvShows(query: String = ""): List<TvShow> {
        val duplicateRemoverSet = mutableSetOf<Result>()
        val temp = repository.getTVShows(query).results
        temp.forEach {
            duplicateRemoverSet.add(it)
        }
        val tvShowList = AppMapper.mapGetTvShowsApiResultToTvShowList(duplicateRemoverSet.toList())
        _tvShowList.update { tvShowList }
        return tvShowList
    }

    private suspend fun getRecentTvShows(){
        val duplicateRemoverSet = mutableSetOf<TvShow>()
        repository.getRecentTvShows().forEach {
            duplicateRemoverSet.add(it)
        }
        _recentTvShowList.update { duplicateRemoverSet.toList() }
    }

    fun insertRecentTvShow(tvShow: TvShow) {
        viewModelScope.launch {
            repository.insertRecentTvShow(tvShow = tvShow)
        }
    }

    fun getTvShowById(tvShowId: Int) {
        viewModelScope.launch {
            val apiResult = repository.getTVShowById(tvShowId)
            val result = AppMapper.mapGetTvShowByIdApiResultToTvShow(apiResult)
            _selectedTvShow.update { result }
        }
    }

    fun getTvShowSeasons(tvShowId: Int, seasonNumber: Int = 1) {
        viewModelScope.launch {
            _isEpisodesLoading.value = true
            val apiResult = repository.getTvShowSeason(tvShowId, seasonNumber)
            val result = AppMapper.mapGetTvShowSeasonsApiResultToTvShowSeason(apiResult)
            result.forEach {
                it.isChecked = repository.getIsWatchedStatus(it.episodeId)
                insertTvShowSeasonEpisodesToDB(it)
            }
            _selectedSeason.update { result }
            _isEpisodesLoading.value = false
        }
    }

    private fun insertTvShowSeasonEpisodesToDB(tvShowSeasonEpisodes: TvShowSeasonEpisodes) {
        viewModelScope.launch {
            repository.insertTvShow(tvShowSeasonEpisodes)
        }
    }

    fun updateIsWatchedState(episodeId: Int, isWatched: Boolean) {
        viewModelScope.launch {
            repository.updateIsWatchedStatus(episodeId = episodeId, isWatchedStatus = isWatched)
        }
    }
}