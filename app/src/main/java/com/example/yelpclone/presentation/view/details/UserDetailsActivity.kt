package com.example.yelpclone.presentation.view.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yelpclone.data.model.users.UserList
import com.example.yelpclone.data.model.yelp.YelpRestaurants
import com.example.yelpclone.databinding.ActivityDetailsBinding
import com.example.yelpclone.presentation.view.maps.UserMapActivity
import com.example.yelpclone.presentation.view.restaurant.RestaurantsActivity
import com.example.yelpclone.presentation.view.user.UserActivity
import com.example.yelpclone.presentation.view.user.UserActivity.Companion.EXTRA_ITEM_ID
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val USER_DETAIL = "DETAIL_ACTIVITY"
        const val EXTRA_ITEM_ID = "EXTRA_ITEM_ID"
    }

    init {
        Log.d(USER_DETAIL, "DetailsActivity created.")
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayUserInfo()
        backPressed()
    }

    private fun backPressed() = onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@UserDetailsActivity, UserActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

    @SuppressLint("SetTextI18n")
    private fun displayUserInfo() {
        val userDetails = intent.getParcelableExtra<UserList>(UserActivity.EXTRA_ITEM_ID)
        binding.apply {
            userDetails?.let {

                val mapIntent = Intent(
                    this@UserDetailsActivity,
                    UserMapActivity::class.java
                )

                val mapBundle = Bundle().apply {
                    mapIntent.putExtra(EXTRA_ITEM_ID, it)
                }
                mapsButton.setOnClickListener {
                    startActivity(mapIntent)
                    finish()
                }

                // scale and transform image to our needs using Glide.
                Glide.with(ivImage.context)
                    .load(userDetails.avatar)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(), RoundedCorners(10)
                        )
                    )
                    .into(ivImage)

                tvName.text = "${it.firstname} ${it.lastName}"
                tvEmployment.text = "CAREER: ${it.employment.title}"
                tvEmail.text = "EMAIL: ${it.email}"
                tvAddress.text = "ADDRESS: ${it.address.streetName}"
                tvCityOrState.text = "STATE: ${it.address.state}"
                tvStateOrCountry.text = "COUNTRY: ${it.address.country}"
                tvCountryOrZipcode.text = " ZIPCODE: ${it.address.zipCode}"
                userPhoneNumber.text = "PHONE: ${it.phoneNumber}"
                tvCategoryOrSkill.text = "SKILL: ${it.employment.skill}"
                Log.d(USER_DETAIL, "User Details initialized.")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}