package com.example.yelpclone.domain.repository

import com.example.yelpclone.data.api.YelpService
import com.example.yelpclone.data.model.YelpSearchResult
import com.example.yelpclone.domain.util.Constants
import com.example.yelpclone.domain.util.Resource

/*
Implementing methods outlined in our repository. Serves as layer between api and views.
 */
class RepositoryImpl(
    private val yelpService: YelpService
) : YelpRepository {

    override suspend fun searchRestaurants(
        authHeader: String,
        searchTerm: String,
        location: String
    ): Resource<YelpSearchResult> {
        return try {
            val response = yelpService.searchRestaurants(
                "Bearer ${Constants.API_KEY}",
                "restaurants",
                "Florida"
            )
            val result = response.body()
            if ((response.isSuccessful) && (result != null)) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to retrieve rates.")
        }
    }
}

