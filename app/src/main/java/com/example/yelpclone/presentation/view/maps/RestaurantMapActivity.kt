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

    /*
    Variables we will use to set the information received from out api on the map.
     */
    private var lat: Double = 0.0
    private var lng: Double = 0.0
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

        val yelpDetails =
            intent.getParcelableExtra<YelpRestaurants>(RestaurantsActivity.EXTRA_ITEM_ID_MAIN)

        binding.apply {
            yelpDetails?.let {
                lat = it.coordinates.latitude
                lng = it.coordinates.longitude
                coordinates = LatLng(it.coordinates.latitude, it.coordinates.longitude)
                title = it.name
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(MarkerOptions().position(coordinates!!).title(title!!))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates!!))

        // swipe mechanism
        backPressed()
    }

    private fun backPressed() = onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(
                    this@RestaurantMapActivity,
                    RestaurantsActivity::class.java)
                startActivity(intent)
            }
        }
    )
}