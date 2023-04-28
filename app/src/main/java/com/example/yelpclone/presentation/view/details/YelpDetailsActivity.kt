package com.example.yelpclone.presentation.view.details

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yelpclone.data.model.yelp.YelpRestaurants
import com.example.yelpclone.databinding.ActivityYelpDetailsBinding
import com.example.yelpclone.presentation.view.maps.RestaurantMapActivity
import com.example.yelpclone.presentation.view.restaurant.RestaurantsActivity

class YelpDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityYelpDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val YELP_DETAILS = "YELP_DETAIL_ACTIVITY"
    }

    init {
        Log.d(YELP_DETAILS, "DetailsActivity created.")
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityYelpDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayMainInfo()
        backPressed()
    }

    private fun backPressed() = onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@YelpDetailsActivity, RestaurantsActivity::class.java)
                startActivity(intent)
            }
        }
    )

    @SuppressLint("SetTextI18n")
    private fun displayMainInfo() {
        val yelpDetails =
            intent.getParcelableExtra<YelpRestaurants>(RestaurantsActivity.EXTRA_ITEM_ID_MAIN)
        binding.apply {
            yelpDetails?.let {

                // Navigating to maps activity
                val mapIntent = Intent(
                    this@YelpDetailsActivity,
                    RestaurantMapActivity::class.java
                )

                val mapBundle = Bundle().apply {
                    mapIntent.putExtra(RestaurantsActivity.EXTRA_ITEM_ID_MAIN, it)
                }

                mapsButton.setOnClickListener {
                    startActivity(mapIntent)
                }

                // scale and transform image to our needs using Glide.
                Glide.with(ivImage.context)
                    .load(yelpDetails.imageUrl)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(), RoundedCorners(10)
                        )
                    )
                    .into(ivImage)

                tvRating.visibility = View.VISIBLE.apply {
                    tvRating.rating = it.rating.toFloat()
                }
                tvReviews.visibility = View.VISIBLE.apply {
                    tvReviews.text = "${it.reviewCount} Reviews"
                }
                tvName.text = it.name
                tvDistance.text = it.displayDistance()
                tvAddress.text = it.location.address1
                tvCityOrState.text = "${it.location.city}, ${it.location.state}, "
                tvStateOrCountry.text = "${it.location.country} "
                tvCountryOrZipcode.text = it.location.zipCode
                userPhoneNumber.text = it.phone
                tvCategoryOrSkill.text = it.categories[0].title
                Log.d(YELP_DETAILS, "Yelp Restaurants initialized.")
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}