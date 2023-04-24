package com.example.yelpclone.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yelpclone.data.model.YelpSearchResult
import com.example.yelpclone.domain.repository.RepositoryImpl
import com.example.yelpclone.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl
) : ViewModel() {

    companion object {
        private const val VIEW_MODEL = "MAIN_VIEW_MODEL"
    }

    init {
        Log.d(VIEW_MODEL, "View model initialized.")
    }

    private var _searchState: MutableStateFlow<Resource<YelpSearchResult?>> = MutableStateFlow(Resource.Loading())
    val searchState: MutableStateFlow<Resource<YelpSearchResult?>> get() = _searchState

    fun getRestaurants(authHeader: String, searchTerm: String, location: String) {
        viewModelScope.launch(Dispatchers.IO) {

            delay(1000)

            try {
                when (val apiResult =
                    repositoryImpl.searchRestaurants(authHeader, searchTerm, location)) {

                    is Resource.Loading -> {
                        _searchState.value = Resource.Loading()
                        Log.d(VIEW_MODEL, "Loading restaurants.")
                    }

                    is Resource.Error -> {
                        _searchState.value = Resource.Error(apiResult.message.toString())
                        Log.d(VIEW_MODEL, "FAILED to find data: ${apiResult.message}")
                    }

                    is Resource.Success -> {
                        _searchState.value = Resource.Success(apiResult.data)
                        Log.d(VIEW_MODEL, "SUCCESSFULLY found data! : ${apiResult.data}")
                    }
                }

            } catch (e: Exception) {
                Log.e(VIEW_MODEL, "Error getting restaurants", e)
            }
        }
    }
}