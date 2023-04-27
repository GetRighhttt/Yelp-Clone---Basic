package com.example.yelpclone.domain

import com.example.yelpclone.data.api.YelpService
import com.example.yelpclone.data.model.yelp.YelpSearchResult
import com.example.yelpclone.core.events.Resource
import com.example.yelpclone.data.model.users.UserList
import com.example.yelpclone.domain.sot.YelpRepository
import javax.inject.Inject
import javax.inject.Singleton

/*
Implementing methods outlined in our repository. Serves as layer between api and views.
 */
@Singleton
class RepositoryImpl @Inject constructor (
    private val yelpService: YelpService
) : YelpRepository {

    override suspend fun searchRestaurants(
        authHeader: String,
        searchTerm: String,
        location: String,
        limit: Int,
        offset: Int
    ): Resource<YelpSearchResult> {
        return try {
            val response = yelpService.searchRestaurants(
                authHeader,
                searchTerm,
                location,
                limit,
                offset
            )
            val result = response.body()
            if ((response.isSuccessful) && (result != null)) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to retrieve restaurants.")
        }
    }

    override suspend fun getUsers(size: Int): Resource<List<UserList>> {
        return try {
           val response = yelpService.getUsers(size)
            val result = response.body()
            if ((response.isSuccessful) && (result != null)) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        }catch (e: Exception) {
            Resource.Error(e.message ?: "Unable to retrieve users.")
        }
    }
}

