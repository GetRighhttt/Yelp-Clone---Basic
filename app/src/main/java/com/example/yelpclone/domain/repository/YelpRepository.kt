package com.example.yelpclone.domain.repository

import com.example.yelpclone.domain.util.Resource

/*
Repositories serve as single source of truth to link data to domain models.
 */
interface YelpRepository {
    suspend fun searchRestaurants(authHeader: String, searchTerm: String, location: String): Resource<Any>
}