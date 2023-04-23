package com.example.yelpclone.domain.repository

import com.example.yelpclone.data.model.YelpSearchResult
import com.example.yelpclone.domain.util.Resource

/*
Acts as a single source of truth and additional layer of abstraction.
Isolates the data layer from the rest of the application.
The methods defined here wil be implemented in our repositoryImpl, which will
then be used as a constructor dependency in our view model.
 */
interface YelpRepository {
    suspend fun searchRestaurants(authHeader: String, searchTerm: String, location: String): Resource<YelpSearchResult>
}