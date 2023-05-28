package com.example.yelpclone.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yelpclone.data.model.yelp.YelpBusinesses
import kotlinx.coroutines.flow.Flow

@Dao
interface BusinessDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertABusiness(business: YelpBusinesses)

    @Delete
    suspend fun deleteABusiness(business: YelpBusinesses)

    // do not need to call these from a background thread so we do not suspend these method
    @Query("SELECT * FROM businesses")
    fun getAllBusinesses(): Flow<List<YelpBusinesses>>

    @Query("DELETE FROM businesses")
    fun deleteAllBusinesses()
}