package com.example.yelpclone.presentation.viewmodel.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yelpclone.core.util.Constants
import com.example.yelpclone.data.model.yelp.YelpSearchResult
import com.example.yelpclone.domain.RepositoryImpl
import com.example.yelpclone.core.util.DispatcherProvider
import com.example.yelpclone.core.events.Resource
import com.example.yelpclone.core.events.SearchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var _searchState: MutableStateFlow<SearchEvent<YelpSearchResult?>> = MutableStateFlow(
        SearchEvent.Idle()
    )
    val searchState: MutableStateFlow<SearchEvent<YelpSearchResult?>> get() = _searchState

    companion object {
        private const val RESTAURANT_VIEW_MODEL = "MAIN_VIEW_MODEL"
        private const val BEARER = "Bearer ${Constants.RESTAURANT_API_KEY}"
        private const val DEFAULT_SEARCH_TERM = "Seafood"
        private const val DEFAULT_LOCATION = "Tampa"
        private const val DEFAULT_LIMIT = 50
        private const val DEFAULT_OFFSET = 0
    }

    init {
        // set the initial state to Loading() so the progress bar shows
        _searchState.value = SearchEvent.Loading()

        // get the restaurants as soon as the view model is initialized
        getRestaurants()
        Log.d(
            RESTAURANT_VIEW_MODEL,
            "View Model is initialized. getRestaurants() method has been called. "
        )
    }

    private fun getRestaurants(query: String = DEFAULT_SEARCH_TERM) {
        viewModelScope.launch(dispatcherProvider.ioCD) {
            // delay to show our progress bar
            delay(500)

            try {
                when (val apiResult =
                    repositoryImpl.searchRestaurants(
                        BEARER,
                        DEFAULT_SEARCH_TERM,
                        DEFAULT_LOCATION,
                        DEFAULT_LIMIT,
                        DEFAULT_OFFSET
                    )) {

                    is Resource.Loading -> {
                        _searchState.value = SearchEvent.Loading()
                        Log.d(RESTAURANT_VIEW_MODEL, "Loading restaurants.")
                    }

                    is Resource.Error -> {
                        _searchState.value = SearchEvent.Failure(apiResult.message.toString())
                        Log.d(RESTAURANT_VIEW_MODEL, "FAILED to find data: ${apiResult.message}")
                    }

                    is Resource.Success -> {
                        _searchState.value = SearchEvent.Success(apiResult.data)
                        Log.d(RESTAURANT_VIEW_MODEL, "SUCCESSFULLY found data! : ${apiResult.data}")
                    }
                }

            } catch (e: Exception) {
                Log.e(RESTAURANT_VIEW_MODEL, "Error getting restaurants!", e)
            }
        }
    }

    override fun onCleared() {
        Log.d(RESTAURANT_VIEW_MODEL, "Cleared.")
    }
}