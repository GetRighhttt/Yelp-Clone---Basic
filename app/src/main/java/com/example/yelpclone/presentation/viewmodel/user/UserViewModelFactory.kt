package com.example.yelpclone.presentation.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yelpclone.core.util.DispatcherProvider
import com.example.yelpclone.domain.UserRepositoryImpl

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(
    private val repositoryImpl: UserRepositoryImpl,
    private val dispatcherProvider: DispatcherProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repositoryImpl, dispatcherProvider) as T
    }
}