package com.example.yelpclone.presentation.view.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.yelpclone.R
import com.example.yelpclone.domain.model.users.UserList

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.yelpclone.databinding.ActivityUserMapBinding
import com.example.yelpclone.presentation.view.details.UserDetailsActivity
import com.example.yelpclone.presentation.view.activity.UserActivity

class UserMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityUserMapBinding
    private var coordinates: LatLng? = null
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setUserDetailsFromIntents()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(MarkerOptions().position(coordinates!!).title(title!!))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates!!))
        // swipe mechanism
        onBackPressedInitializer()
    }

    private fun setUserDetailsFromIntents() {
        val userLat =
            intent.getParcelableExtra<UserList>(UserDetailsActivity.EXTRA_ITEM_ID)!!
                .address.coordinates.latitude
        val userLon =
            intent.getParcelableExtra<UserList>(UserDetailsActivity.EXTRA_ITEM_ID)!!
                .address.coordinates.longitude
        val userTitle =
            intent.getParcelableExtra<UserList>(UserDetailsActivity.EXTRA_ITEM_ID)!!
                .address.streetName
        coordinates = LatLng(userLat, userLon)
        title = userTitle
    }

    private fun onBackPressedInitializer() = onBackPressedDispatcher.addCallback(
        this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(
                    this@UserMapActivity,
                    UserActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
    )
}