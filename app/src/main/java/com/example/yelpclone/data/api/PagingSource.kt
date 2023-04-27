package com.example.yelpclone.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yelpclone.data.model.YelpRestaurants

/*
The Yelp api does not provide a paging parameter that we can read in. They also have a limit of
only 50 total results, so for this example, we will not use the Paging3 library.
 */
class PagingSource{
//    private val yelpService: YelpService,
//    private val query: String
//    ) : PagingSource<Int, YelpRestaurants>() {
//
//    override fun getRefreshKey(state: PagingState<Int, YelpRestaurants>): Int? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, YelpRestaurants> {
//        TODO("Not yet implemented")
//    }
}