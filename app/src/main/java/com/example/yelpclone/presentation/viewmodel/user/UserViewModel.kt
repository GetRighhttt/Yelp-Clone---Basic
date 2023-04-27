package com.example.yelpclone.presentation.viewmodel.user

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.yelpclone.core.util.DispatcherProvider
import com.example.yelpclone.domain.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl,
    private val dispatcherProvider: DispatcherProvider
): ViewModel() {

    companion object {
        private const val USER_VIEW_MODEL = "USER_VIEW_MODEL"
    }

    init {
        Log.d(USER_VIEW_MODEL, "User View Model is initialized. getRestaurants() method has been called. ")
    }
}