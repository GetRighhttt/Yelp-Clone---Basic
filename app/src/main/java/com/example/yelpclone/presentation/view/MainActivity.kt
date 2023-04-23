package com.example.yelpclone.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.yelpclone.R
import com.example.yelpclone.data.api.RetrofitInstance
import com.example.yelpclone.databinding.ActivityMainBinding
import com.example.yelpclone.domain.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!


    companion object {
        private const val MAIN = "MAIN_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get a reference to our retrofit object
        val yelpService = RetrofitInstance.retrofit

        // how we make our api call.
        yelpService.also {
            try {
                lifecycleScope.launch {
                    binding.textView.text = it.searchRestaurants(
                        "Bearer ${Constants.API_KEY}",
                        "Avocado Toast",
                        "Florida")
                        .toString()

                    Log.d(MAIN, "Search Restaurants ${binding.textView}")
                }
            } catch (e: Exception) {
                Log.d(MAIN, "Exception while searching for restaurants: ${e.stackTrace}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}