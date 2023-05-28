package com.example.yelpclone.domain.sot

import com.example.yelpclone.core.events.Resource
import com.example.yelpclone.data.model.yelp.YelpBusinesses
import com.example.yelpclone.data.model.yelp.YelpSearchResult

/*
Acts as a single source of truth and additional layer of abstraction.
Isolates the data layer from the rest of the application.
The methods defined here wil be implemented in our repositoryImpl, which will
then be used as a constructor dependency in our view model.
 */
interface YelpRepository {
    suspend fun searchBusinesses(
        authHeader: String,
        searchTerm: String,
        location: String,
        limit: Int,
        offset : Int): Resource<YelpSearchResult>

    suspend fun insertBusiness(business: YelpBusinesses)
}