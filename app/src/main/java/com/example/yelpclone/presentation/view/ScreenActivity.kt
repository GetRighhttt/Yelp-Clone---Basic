package com.example.yelpclone.presentation.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.yelpclone.databinding.ActivitySplashScreenBinding

class ScreenActivity : AppCompatActivity() {
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivYelp.animate().apply {
            duration = 300L
            rotationXBy(180F)
            rotationYBy(180F)
        }.withEndAction {
            binding.ivYelp.animate().apply {
                duration = 500L
                rotationXBy(-180F)
                rotationYBy(-180F)
            }
        }

        // delays before moving to other activity
    Handler(Looper.getMainLooper()).postDelayed(
    {
        startActivity(
            Intent(this@ScreenActivity, MainActivity::class.java)
        )
        finish()
    }, 2000)
}

override fun onDestroy() {
    super.onDestroy()
    _binding = null
}
}