package com.example.yelpclone.presentation.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelpclone.R
import com.example.yelpclone.core.events.SearchEvent
import com.example.yelpclone.databinding.ActivityMainBinding
import com.example.yelpclone.presentation.view.splashscreens.SecondStartActivity
import com.example.yelpclone.presentation.view.adapter.RestaurantsAdapter
import com.example.yelpclone.presentation.view.details.DetailsActivity
import com.example.yelpclone.presentation.view.user.UserActivity
import com.example.yelpclone.presentation.viewmodel.main.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        const val EXTRA_ITEM_ID = "EXTRA_ITEM_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        determineSearchState()
        menuItemSelection()
    }

    private fun menuItemSelection() {
        binding.apply {
            topAppBar.setNavigationOnClickListener {
                materialDialog(
                    this@MainActivity,
                    "Menu!".uppercase(),
                    "This button would normally display a menu of other options!" +
                            " Click ok to go to Yelp user list, otherwise click cancel to exit."
                )
            }.also {
                topAppBar.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.user -> {
                            materialDialog(
                                this@MainActivity,
                                "Navigation!".uppercase(),
                                "To see a list of Yelp users, click OK. " +
                                        "Otherwise, click cancel to exit."
                            )
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvRestaurantList.apply {
            hasFixedSize()
            yelpAdapter =
                RestaurantsAdapter(this@MainActivity)
            adapter = yelpAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }.also {
            it.smoothScrollToPosition(0)
        }
    }

    private fun determineSearchState() {
        binding.apply {
            lifecycleScope.launch {
                /*
                Flow isn't lifecycle aware so we must flow with lifecycle to handle resource
                consumption.
                */
                viewModel.searchState.flowWithLifecycle(lifecycle).collect { response ->

                    when (response) {
                        is SearchEvent.Failure -> {
                            createSnackBar("Error when fetching Data!")
                            pbMain.visibility = View.GONE
                            Log.d(MAIN, "Failed to update UI with data: ${response.errorMessage}")
                        }

                        is SearchEvent.Loading -> {
                            createSnackBar("Loading...")
                            pbMain.visibility = View.VISIBLE
                            Log.d(MAIN, "Loading main...")
                        }

                        is SearchEvent.Success -> {
                            if (response.results!!.restaurants.isEmpty()) {
                                createSnackBar("Results are Empty!")
                                pbMain.visibility = View.GONE
                                noResults.visibility = View.VISIBLE
                                Log.d(
                                    MAIN,
                                    "Failed to update UI with data: ${response.errorMessage}"
                                )

                            } else {
                                response.results.let {
                                    yelpAdapter.differ.submitList(it.restaurants.toList())
                                        yelpAdapter.setOnItemClickListener {
                                        val detailIntent =
                                            Intent(this@MainActivity, DetailsActivity::class.java)
                                        val bundle = Bundle().apply {
                                            detailIntent.putExtra(MainActivity.EXTRA_ITEM_ID, it)
                                        }
                                        startActivity(detailIntent)
                                    }
                                }
                                createSnackBar("Successfully fetched Data!")
                                pbMain.visibility = View.GONE
                                Log.d(
                                    MAIN,
                                    "Successfully updated UI with data: ${response.results}"
                                )
                            }
                        }

                        is SearchEvent.Idle -> {
                            Log.d(MAIN, "Idle State currently...")
                        }
                    }
                }
            }
        }
    }

    private fun materialDialog(
        context: Context,
        title: String,
        message: String
    ) = object : MaterialAlertDialogBuilder(this) {
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("OK") { _, _ ->
                val intent = Intent(this@MainActivity, SecondStartActivity::class.java)
                startActivity(intent)
            }
            .show()
    }

    private fun createSnackBar(message: String) = Snackbar.make(
        binding.root, message, Snackbar.LENGTH_SHORT
    ).show()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}