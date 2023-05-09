package com.example.yelpclone.presentation.view.maps

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.yelpclone.R
import com.example.yelpclone.data.model.yelp.YelpRestaurants
import com.example.yelpclone.databinding.ActivityRestaurantMapBinding
import com.example.yelpclone.presentation.view.restaurant.RestaurantsActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class RestaurantMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityRestaurantMapBinding
    private var coordinates: LatLng? = null
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestaurantMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setYelpDetails()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(MarkerOptions().position(coordinates!!).title(title!!).draggable(true))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates!!))

        // swipe mechanism
        backPressed()

    }

    private fun setYelpDetails() {
        val yelpLat =
            intent.getParcelableExtra<YelpRestaurants>(RestaurantsActivity.EXTRA_ITEM_ID_MAIN)!!.coordinates.latitude
        val yelpLon =
            intent.getParcelableExtra<YelpRestaurants>(RestaurantsActivity.EXTRA_ITEM_ID_MAIN)!!.coordinates.longitude
        val yelpTitle =
            intent.getParcelableExtra<YelpRestaurants>(RestaurantsActivity.EXTRA_ITEM_ID_MAIN)!!.name
        coordinates = LatLng(yelpLat, yelpLon)
        title = yelpTitle
    }

    private fun backPressed() = onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(
                    this@RestaurantMapActivity,
                    RestaurantsActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
    )
}