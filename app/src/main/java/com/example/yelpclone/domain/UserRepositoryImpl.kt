package com.example.yelpclone.domain

import com.example.yelpclone.core.events.Resource
import com.example.yelpclone.data.api.ApiService
import com.example.yelpclone.data.api.UserService
import com.example.yelpclone.data.model.users.UserList
import com.example.yelpclone.domain.sot.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: UserService
) : UserRepository {
    override suspend fun getUsers(size: Int): Resource<List<UserList>> {
        return try {
            val response = apiService.getUsers(size)
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