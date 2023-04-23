package com.example.yelpclone.domain.util

/*
Interface to handle state for when we search our API.
 */
sealed interface SearchEvent {
    data class SearchSuccess(val successMsg: String) : SearchEvent
    data class SearchFailure(val errorMsg: String) : SearchEvent
    object Empty : SearchEvent
    object Loading : SearchEvent
}