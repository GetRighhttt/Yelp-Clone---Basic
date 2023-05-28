package com.example.yelpclone.presentation.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yelpclone.data.model.yelp.YelpBusinesses
import com.example.yelpclone.domain.sot.SavedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val savedRepository: SavedRepository
) : ViewModel() {

    private val _databaseState = MutableLiveData<List<YelpBusinesses>>()
    val databaseState: MutableLiveData<List<YelpBusinesses>> get() = _databaseState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    init {
        getAllSavedBusinesses()
    }

    private fun getAllSavedBusinesses() = viewModelScope.launch {
        _isLoading.postValue(true)
        delay(1000)
        savedRepository.executeGetAllBusinesses().collectLatest {
            _databaseState.postValue(it)
            _isLoading.postValue(false)
        }
    }

    fun addBusiness(business: YelpBusinesses) = viewModelScope.launch(Dispatchers.IO) {
        _isLoading.postValue(true)
        delay(1000)
        savedRepository.executeInsertBusiness(business)
        _isLoading.postValue(false)
    }

    fun deleteBusiness(business: YelpBusinesses) = viewModelScope.launch(Dispatchers.IO) {
        _isLoading.postValue(true)
        delay(1000)
        savedRepository.executeDeleteABusiness(business)
        _isLoading.postValue(false)
    }

    fun deleteAllBusinesses() = viewModelScope.launch(Dispatchers.IO) {
        _isLoading.postValue(true)
        savedRepository.executeDeleteAllBusinesses()
        _isLoading.postValue(false)
    }

}