package com.example.yelpclone.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SearchEvent
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelpclone.data.api.RetrofitInstance
import com.example.yelpclone.data.api.YelpService
import com.example.yelpclone.data.model.YelpRestaurants
import com.example.yelpclone.databinding.ActivityMainBinding
import com.example.yelpclone.domain.repository.RepositoryImpl
import com.example.yelpclone.domain.util.Constants
import com.example.yelpclone.domain.util.DispatcherProvider
import com.example.yelpclone.domain.util.Resource
import com.example.yelpclone.presentation.viewmodel.MainViewModel
import com.example.yelpclone.presentation.viewmodel.MainViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    // get a reference to the adapter and list recycler view
    private val restaurants = mutableListOf<YelpRestaurants>()
    private val adapter = RestaurantsAdapter(this)

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    companion object {
        private const val MAIN = "MAIN_ACTIVITY"
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        viewModel.getRestaurants(
            "Bearer ${Constants.API_KEY}",
            "Avocado Toast",
            "Florida")

        binding.apply {
            rvRestaurantList.adapter = adapter
            rvRestaurantList.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        determineSearchState()
    }

    private fun determineSearchState() {

        try {
            lifecycleScope.launch {
                binding.apply {
                    viewModel.searchState.collect { searchEvent ->

                        when (searchEvent) {
                            is MainViewModel.SearchEvent.SearchSuccess -> {
                                adapter.differ.submitList(restaurants)
                                materialDialog(
                                    this@MainActivity,
                                    "SUCCESS!",
                                    searchEvent.successMsg
                                )
                                pbMain.visibility = View.GONE
                                Log.d(MAIN, "Search Success!")
                            }

                            is MainViewModel.SearchEvent.SearchFailure -> {
                                materialDialog(
                                    this@MainActivity,
                                    "FAILURE!",
                                    searchEvent.errorMsg
                                )
                                pbMain.visibility = View.GONE
                                Log.d(MAIN, "Search Failure!")
                            }

                            MainViewModel.SearchEvent.Loading -> {
                                Snackbar.make(binding.root, "Loading...", Snackbar.LENGTH_LONG)
                                    .show()
                                pbMain.visibility = View.VISIBLE
                                Log.d(MAIN, "Empty search state")
                            }

                            MainViewModel.SearchEvent.Empty -> {
                                pbMain.visibility = View.GONE
                                Log.d(MAIN, "Empty search state")
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.d(MAIN, "Error: ${e.message}", e)
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
            .setPositiveButton("Continue") { dialog, _ ->
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