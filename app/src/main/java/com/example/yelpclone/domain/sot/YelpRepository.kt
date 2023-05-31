package com.example.yelpclone.domain.sot

import com.example.yelpclone.core.events.Resource
import com.example.yelpclone.data.model.yelp.YelpBusinesses
import com.example.yelpclone.data.model.yelp.YelpSearchResult

interface YelpRepository {
    suspend fun searchBusinesses(
        authHeader: String,
        searchTerm: String,
        location: String,
        limit: Int,
        offset : Int): Resource<YelpSearchResult>
}