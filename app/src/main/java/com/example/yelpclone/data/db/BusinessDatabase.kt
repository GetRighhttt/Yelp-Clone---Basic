package com.example.yelpclone.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yelpclone.data.model.yelp.YelpBusinesses

@Database(entities = [YelpBusinesses::class], version = 1, exportSchema = false)
abstract class BusinessDatabase : RoomDatabase() {
    abstract fun getBusinessDAO(): BusinessDAO
}