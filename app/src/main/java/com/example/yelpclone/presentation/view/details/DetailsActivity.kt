package com.example.yelpclone.presentation.view.details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yelpclone.data.model.users.UserList
import com.example.yelpclone.data.model.yelp.YelpRestaurants
import com.example.yelpclone.databinding.ActivityDetailsBinding
import com.example.yelpclone.presentation.view.main.MainActivity
import com.example.yelpclone.presentation.view.user.UserActivity
import com.example.yelpclone.presentation.viewmodel.main.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()

    companion object {
        const val DETAIL = "DETAIL_ACTIVITY"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayUserInfo()
        backPressed()
    }

    private fun backPressed() =  onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@DetailsActivity, UserActivity::class.java)
            startActivity(intent)
        }
    })

    @SuppressLint("SetTextI18n")
    private fun displayMainInfo() {
        val yelpDetails = intent.getParcelableExtra<YelpRestaurants>(MainActivity.EXTRA_ITEM_ID)
        binding.apply {
            yelpDetails?.let {
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
                tvAddress.text = it.location.address1
                tvCityOrState.text = "${it.location.city}, ${it.location.state}"
                tvStateOrCountry.text = "${it.location.country}, "
                tvCountryOrZipcode.text = it.location.zipCode
                userPhoneNumber.text = it.phone
                tvCategoryOrSkill.text = it.categories[0].title
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayUserInfo() {
        val userDetails = intent.getParcelableExtra<UserList>(UserActivity.EXTRA_ITEM_ID)
        binding.apply {
            userDetails?.let {
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
                tvAddress.text = it.address.streetName
                tvCityOrState.text = "${it.address.state}, "
                tvStateOrCountry.text = "${it.address.country}, "
                tvCountryOrZipcode.text = it.address.zipCode
                userPhoneNumber.text = it.phoneNumber
                tvCategoryOrSkill.text = it.employment.skill
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}