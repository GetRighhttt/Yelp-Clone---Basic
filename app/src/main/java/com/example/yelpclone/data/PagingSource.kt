package com.example.yelpclone.data

import android.os.Parcel
import android.os.Parcelable
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yelpclone.core.util.Constants
import com.example.yelpclone.data.api.YelpService
import com.example.yelpclone.data.model.YelpRestaurants

/*
Implementing Paging3 Jetpack component.
 */
class PagingSource(
    private val yelpService: YelpService,
    private val query: String
): PagingSource<Int, YelpRestaurants>() {

    override fun getRefreshKey(state: PagingState<Int, YelpRestaurants>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, YelpRestaurants> {
        val position = params.key?: Constants.STARTING_PAGE_INDEX

        val response = yelpService.searchRestaurants()
    }

}