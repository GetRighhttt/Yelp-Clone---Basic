package com.example.yelpclone.domain.sot

//@Singleton
//class SavedRepository @Inject constructor(private val businessDAO: BusinessDAO) {
//
//    suspend fun executeInsertBusiness(business: YelpBusinesses) = withContext(Dispatchers.IO) {
//        businessDAO.insertABusiness(business)
//    }
//
//    suspend fun executeDeleteABusiness(business: YelpBusinesses) = withContext(Dispatchers.IO) {
//        businessDAO.deleteABusiness(business)
//    }
//
//    fun executeGetAllBusinesses(): Flow<List<YelpBusinesses>> = businessDAO.getAllBusinesses()
//
//    fun executeDeleteAllBusinesses() = businessDAO.deleteAllBusinesses()
//}