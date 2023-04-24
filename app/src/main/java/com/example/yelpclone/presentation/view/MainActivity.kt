package com.example.yelpclone.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelpclone.data.model.YelpCoordinates
import com.example.yelpclone.data.model.YelpRestaurants
import com.example.yelpclone.databinding.ActivityMainBinding
import com.example.yelpclone.domain.util.Constants
import com.example.yelpclone.domain.util.Resource
import com.example.yelpclone.presentation.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private lateinit var yelpAdapter: RestaurantsAdapter

    companion object {
        private const val MAIN = "MAIN_ACTIVITY"
        private const val BEARER = "Bearer ${Constants.API_KEY}"
        private const val SEARCH_TERM = "Avocado Toast"
        private const val LOCATION = "New York"
        const val EXTRA_ID = "EXTRA_ID"
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
            yelpAdapter =
                RestaurantsAdapter(this@MainActivity, object : RestaurantsAdapter.OnClickListener {
                    override fun onItemClick(position: Int) {
                        navigateToMaps(position)
                    }
                })
            adapter = yelpAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }.also {
            it.smoothScrollToPosition(0)
        }
    }

    private fun setRestaurants() = viewModel.getRestaurants(BEARER, SEARCH_TERM, LOCATION)

    private fun navigateToMaps(position: Int) = run {
        val intent = Intent(this@MainActivity, MapsActivity::class.java)
        setRestaurants()
        lifecycleScope.launch {
            viewModel.searchState.collect {
                when (it) {
                    is Resource.Success -> {
                        val lat =
                            it.data!!.restaurants[position].coordinates.latitude
                        val long =
                            it.data.restaurants[position].coordinates.longitude
                        intent.putExtra(EXTRA_ID, lat.toString())
                        intent.putExtra(EXTRA_ID, lat.toString())
                    }
                    else -> {
                        Snackbar.make(
                            binding.root,
                            "Couldn't navigate to maps sadly...",
                            LENGTH_LONG
                        ).setAction("Ok") { Unit }.show()
                    }
                }
            }
        }
        startActivity(intent)
    }

    private fun determineSearchState() {
        setRestaurants()
        binding.apply {
            lifecycleScope.launch {
                viewModel.searchState.collect { response ->

                    when (response) {
                        is Resource.Error -> {
                            materialDialog(
                                this@MainActivity,
                                "ERROR!",
                                "Oops! Looks like we couldn't fetch any data!" +
                                        " Try again in a few minutes!"
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
                                "Hooray! We were able to fetch " +
                                        "${response.data!!.total} restaurants!"
                            )
                            pbMain.visibility = View.GONE
                            Log.d(MAIN, "Successfully updated UI with data: ${response.data}")

                        }
                    }
                }
            }
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}