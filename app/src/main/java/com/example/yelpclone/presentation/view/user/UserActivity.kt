package com.example.yelpclone.presentation.view.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yelpclone.R
import com.example.yelpclone.core.events.SearchEvent
import com.example.yelpclone.databinding.ActivityUserBinding
import com.example.yelpclone.presentation.view.adapter.UserAdapter
import com.example.yelpclone.presentation.view.business.YelpActivity
import com.example.yelpclone.presentation.view.details.UserDetailsActivity
import com.example.yelpclone.presentation.viewmodel.main.user.UserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private var _binding: ActivityUserBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    companion object {
        private const val USER = "USER_ACTIVITY"
        const val EXTRA_ITEM_ID = "EXTRA_ITEM_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        determineUserState()
        menuItemSelection()
        setupSearchView()
        backPressed()
    }

    // shared preferences example storing size of recycler view
    override fun onStart() {
        super.onStart()

        val sharedPreferences by lazy { getSharedPreferences("UsersList", MODE_PRIVATE) }
        val lastCount = sharedPreferences.getInt("StartCount", 0)
        val newCount = lastCount + 10
        Log.d("SHARED_PREFERENCES", "SharedPreferences $newCount")
        sharedPreferences.edit().putInt("StartCount", newCount).apply()
    }

    private fun initRecyclerView() {
        binding.rvUserList.apply {
            hasFixedSize()
            userAdapter = UserAdapter(this@UserActivity)
            adapter = userAdapter
            layoutManager = GridLayoutManager(
                this@UserActivity,
                2,
                GridLayoutManager.VERTICAL,
                false
            )
        }.also {
            it.smoothScrollToPosition(0)
        }
    }

    private fun determineUserState() {
        binding.apply {
            lifecycleScope.launch {
                /*
                Flow isn't lifecycle aware so we must flow with lifecycle to handle resource
                consumption.
                */
                userViewModel.userState.flowWithLifecycle(lifecycle).collect { response ->

                    when (response) {
                        is SearchEvent.Failure -> {
                            createSnackBar("Error when fetching Data!")
                            pbUser.visibility = View.GONE
                            Log.d(USER, "Failed to update UI with data: ${response.errorMessage}")
                        }

                        is SearchEvent.Loading -> {
                            createSnackBar("Loading...")
                            pbUser.visibility = View.VISIBLE
                            Log.d(USER, "Loading user activity...")
                        }

                        is SearchEvent.Success -> {
                            if (response.results!!.isEmpty()) {
                                createSnackBar("Results are Empty!")
                                pbUser.visibility = View.GONE
                                noResults.visibility = View.VISIBLE
                                Log.d(
                                    USER, "Failed to update UI with data: ${response.errorMessage}"
                                )

                            } else {
                                response.results.let {
                                    userAdapter.differ.submitList(it.toList())
                                    userAdapter.setOnItemClickListener {
                                        val detailIntent =
                                            Intent(
                                                this@UserActivity,
                                                UserDetailsActivity::class.java
                                            )
                                        val bundle = Bundle().apply {
                                            detailIntent.putExtra(EXTRA_ITEM_ID, it)
                                        }
                                        startActivity(detailIntent)
                                        overridePendingTransition(
                                            R.anim.slide_in_left_animation,
                                            R.anim.slide_out_right
                                        )
                                        finish()
                                    }
                                }
                                createSnackBar("Successfully fetched Data!")
                                pbUser.visibility = View.GONE
                                Log.d(
                                    USER, "Successfully updated UI with data: ${response.results}"
                                )
                            }
                        }

                        is SearchEvent.Idle -> {
                            Log.d(USER, "Idle State currently...")
                        }
                    }
                }
            }
        }
    }

    private fun setupSearchView() = binding.userSearchView.apply {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.pbUser.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        binding.rvUserList.smoothScrollToPosition(0)
                        userViewModel.getUsers(query)
                        clearFocus()
                        delay(1500)
                        binding.pbUser.visibility = View.GONE
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun menuItemSelection() {
        binding.apply {
            topUserAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.main -> {
                        materialDialog(
                            this@UserActivity,
                            "Navigation".uppercase(),
                            "To go back to restaurants, click OK. " +
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

    private fun backPressed() = onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val backIntent = Intent(this@UserActivity, YelpActivity::class.java)
                startActivity(backIntent)
                finish()
            }
        }
    )

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
                val intent = Intent(this@UserActivity, YelpActivity::class.java)
                startActivity(intent)
                finish()
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