package com.example.yelpclone.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelpclone.data.model.YelpRestaurants
import com.example.yelpclone.databinding.ActivityMainBinding
import com.example.yelpclone.domain.util.Constants
import com.example.yelpclone.domain.util.Resource
import com.example.yelpclone.presentation.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private lateinit var yelpAdapter: RestaurantsAdapter
    private val restaurants = mutableListOf<YelpRestaurants>()

    companion object {
        private const val MAIN = "MAIN_ACTIVITY"
        private const val BEARER = "Bearer ${Constants.API_KEY}"
        private const val SEARCH_TERM = "Avocado Toast"
        private const val LOCATION = "New York"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        determineSearchState()
    }

    private fun initRecyclerView() {
        binding.rvRestaurantList.apply {
            yelpAdapter = RestaurantsAdapter(this@MainActivity)
            adapter = yelpAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun determineSearchState() {
        viewModel.getRestaurants(BEARER, SEARCH_TERM, LOCATION)
        binding.apply {

            viewModel.searchState.observe(this@MainActivity, Observer { response ->
                when (response) {
                    is Resource.Error -> {
                        materialDialog(
                            this@MainActivity,
                            "ERROR!",
                            "Oops! Looks like we couldn't fetch any data Try again in a few minutes!"
                        )
                        pbMain.visibility = View.GONE
                        Log.d(MAIN, "Failed to update UI with data: ${response.message}")
                    }

                    is Resource.Loading -> {
                        pbMain.visibility = View.VISIBLE
                        Log.d(MAIN, "Loading main...}")
                    }

                    is Resource.Success -> {
                        response.data?.let {
                            yelpAdapter.differ.submitList(it.restaurants.toList())
                        }
                        materialDialog(
                            this@MainActivity,
                            "SUCCESS!",
                            "Hooray! We were able to fetch ${response.data} restaurants!"
                        )
                        pbMain.visibility = View.GONE
                        Log.d(MAIN, "Successfully updated UI with data: ${response.data}")

                    }
                }
            })
        }
    }

    private fun materialDialog(
        mainActivity: MainActivity,
        titleText: String,
        answerText: String
    ) = object : MaterialAlertDialogBuilder(this) {
        val dialog = MaterialAlertDialogBuilder(mainActivity)
            .setTitle(titleText)
            .setMessage(answerText)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun animateButton(button: Button) = binding.apply {
        button.animate().apply {
            duration = 500L
            rotationXBy(180F)
        }.withEndAction {
            button.animate().apply {
                duration = 500L
                rotationXBy(-180F)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}