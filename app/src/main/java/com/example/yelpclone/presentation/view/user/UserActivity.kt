package com.example.yelpclone.presentation.view.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yelpclone.R
import com.example.yelpclone.databinding.ActivityUserBinding
import com.example.yelpclone.presentation.view.adapter.RestaurantsAdapter
import com.example.yelpclone.presentation.view.adapter.UserAdapter
import com.example.yelpclone.presentation.view.main.MainActivity
import com.example.yelpclone.presentation.viewmodel.user.UserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UserActivity : AppCompatActivity() {

    private var _binding: ActivityUserBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    companion object {
        private const val USER = "USER_ACTIVITY"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun initRecyclerView() {
        binding.rvUserList.apply {
            hasFixedSize()
            userAdapter =
                UserAdapter(this@UserActivity, object : UserAdapter.OnClickListener {
                    override fun onItemClick(position: Int) {
                        Toast.makeText(
                            this@UserActivity,
                            "Item $position clicked!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@UserActivity)
        }.also {
            it.smoothScrollToPosition(0)
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
                val intent = Intent(this@UserActivity, MainActivity::class.java)
                startActivity(intent)
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}