package com.example.yelpclone.presentation.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yelpclone.core.util.DispatcherProvider
import com.example.yelpclone.domain.RepositoryImpl
import com.example.yelpclone.presentation.viewmodel.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory (
    private val repositoryImpl: RepositoryImpl,
    private val dispatcherProvider: DispatcherProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repositoryImpl, dispatcherProvider) as T
    }
}