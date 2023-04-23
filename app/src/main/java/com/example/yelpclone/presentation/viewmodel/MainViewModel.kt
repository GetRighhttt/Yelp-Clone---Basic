package com.example.yelpclone.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yelpclone.domain.repository.RepositoryImpl
import com.example.yelpclone.domain.util.DispatcherProvider
import com.example.yelpclone.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl,
    private val dispatchers: DispatcherProvider,
    private val app: Application
) : AndroidViewModel(app) {

    companion object {
        private const val VIEW_MODEL = "MAIN_VIEW_MODEL"
    }

    init {
        Log.d(VIEW_MODEL, "View model initialized.")
    }

    private var _searchState = MutableStateFlow<SearchEvent>(SearchEvent.Empty)
    val searchState: MutableStateFlow<SearchEvent> = _searchState

    fun getRestaurants(authHeader: String, searchTerm: String, location: String) {
        _searchState.value = SearchEvent.Loading

        try {
            viewModelScope.launch(dispatchers.ioCD) {
                // delay to allow for progress bar to show
                delay(500L)

                val searchResponse =
                    repositoryImpl.searchRestaurants(authHeader, searchTerm, location)

                when (searchResponse) {

                    is Resource.Success -> {
                        val searchSuccess = searchResponse.data.toString()
                        _searchState.value = SearchEvent.SearchSuccess(searchSuccess)
                        if (searchSuccess.isEmpty()) {
                            _searchState.value =
                                SearchEvent.SearchFailure(searchResponse.message.toString())
                            Log.d(VIEW_MODEL, "Search restaurants = FAILURE!")
                        }
                        Log.d(VIEW_MODEL, "Search restaurants = SUCCESS!")
                    }

                    is Resource.Error -> {
                        _searchState.value =
                            SearchEvent.SearchFailure(searchResponse.message.toString())
                        Log.d(VIEW_MODEL, "Search restaurants = FAILURE!")
                    }

                    is Resource.Loading -> {
                        _searchState.value = SearchEvent.Loading
                        Log.d(VIEW_MODEL, "Search restaurants = Loading...")
                    }
                }
            }
        } catch (e: IllegalStateException) {
            _searchState.value = SearchEvent.SearchFailure(e.message.toString())
            Log.d(VIEW_MODEL, "Exception getting restaurants: ${e.printStackTrace()}")
        }
    }

    fun setEmptySearchState() = SearchEvent.Empty
    fun setLoadingSearchState() = SearchEvent.Loading

    sealed class SearchEvent {
        data class SearchSuccess(
            val successMsg: String = "All information has loaded in successfully!"
        ) : SearchEvent()

        data class SearchFailure(
            val errorMsg: String = "There seems to be an error loading in information..."
        ) : SearchEvent()

        object Empty : SearchEvent()
        object Loading : SearchEvent()
    }
}