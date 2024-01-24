package com.example.yelpclone.presentation.viewmodel.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yelpclone.core.events.Resource
import com.example.yelpclone.core.events.SearchEvent
import com.example.yelpclone.core.util.Constants
import com.example.yelpclone.core.util.DispatcherProvider
import com.example.yelpclone.data.model.yelp.YelpBusinesses
import com.example.yelpclone.data.model.yelp.YelpSearchResult
import com.example.yelpclone.domain.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YelpViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private var _searchState: MutableStateFlow<SearchEvent<YelpSearchResult?>> = MutableStateFlow(
        SearchEvent.Idle()
    )
    val searchState: MutableStateFlow<SearchEvent<YelpSearchResult?>> get() = _searchState

    val getBusinesses: (String) -> Unit = { query ->
        viewModelScope.launch(dispatcherProvider.ioCD) {
            // delay to show our progress bar
            delay(200)

            try {
                when (val apiResult =
                    repositoryImpl.searchBusinesses(
                        BEARER,
                        query,
                        DEFAULT_LOCATION,
                        DEFAULT_LIMIT,
                        DEFAULT_OFFSET
                    )) {
                    is Resource.Loading -> {
                        _searchState.value = SearchEvent.Loading()
                        Log.d(YELP_VIEW_MODEL, "Loading businesses.")
                    }

                    is Resource.Error -> {
                        _searchState.value = SearchEvent.Failure(apiResult.message.toString())
                        Log.d(YELP_VIEW_MODEL, "FAILED to find data: ${apiResult.message}")
                    }

                    is Resource.Success -> {
                        _searchState.value = SearchEvent.Success(apiResult.data)
                        Log.d(YELP_VIEW_MODEL, "SUCCESSFULLY found data! : ${apiResult.data}")
                    }
                }

            } catch (e: Exception) {
                Log.e(YELP_VIEW_MODEL, "Error getting businesses!", e)
            }
        }
    }

    init {
        // set the initial state to Loading() so the progress bar shows
        _searchState.value = SearchEvent.Loading()

        // get the restaurants as soon as the view model is initialized
        getBusinesses("")
        Log.d(
            YELP_VIEW_MODEL,
            "View Model is initialized. Method has been called. "
        )
    }

    override fun onCleared() {
        Log.d(YELP_VIEW_MODEL, "Cleared.")
    }

    companion object {
        private const val YELP_VIEW_MODEL = "MAIN_VIEW_MODEL"
        private const val BEARER = "Bearer ${Constants.YELP_API_KEY}"
        private const val DEFAULT_LOCATION = "Tampa"
        private const val DEFAULT_LIMIT = 50
        private const val DEFAULT_OFFSET = 0
    }
}
