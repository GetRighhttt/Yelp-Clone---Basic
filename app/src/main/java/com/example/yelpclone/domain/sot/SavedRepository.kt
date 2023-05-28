package com.example.yelpclone.domain.sot

import com.example.yelpclone.data.db.BusinessDAO
import com.example.yelpclone.data.model.yelp.YelpBusinesses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedRepository @Inject constructor(private val businessDAO: BusinessDAO) {

    suspend fun executeInsertBusiness(business: YelpBusinesses) = withContext(Dispatchers.IO) {
        businessDAO.insertABusiness(business)
    }

    suspend fun executeDeleteABusiness(business: YelpBusinesses) = withContext(Dispatchers.IO) {
        businessDAO.deleteABusiness(business)
    }

    fun executeGetAllBusinesses(): Flow<List<YelpBusinesses>> = businessDAO.getAllBusinesses()

    fun executeDeleteAllBusinesses() = businessDAO.deleteAllBusinesses()
}