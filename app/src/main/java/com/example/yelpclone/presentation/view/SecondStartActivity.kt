package com.example.yelpclone.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.yelpclone.R
import com.example.yelpclone.databinding.ActivitySecondStartBinding
import com.example.yelpclone.presentation.view.main.MainActivity
import com.example.yelpclone.presentation.view.user.UserActivity

class SecondStartActivity : AppCompatActivity() {

    private var _binding: ActivitySecondStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySecondStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.secondIvYelp.animate().apply {
            duration = 250L
            rotationYBy(360F)
        }.withEndAction {
            binding.secondIvYelp.animate().apply {
                rotationYBy(-360F)
            }
            // delays before moving to other activity
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    startActivity(
                        Intent(this@SecondStartActivity, UserActivity::class.java)
                    )
                    finish()
                }, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}