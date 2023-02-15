package com.example.submissionakhirintermediate.ui.pages

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.submissionakhirintermediate.R
import com.example.submissionakhirintermediate.data.model.StoryApp
import com.example.submissionakhirintermediate.databinding.ActivityMapsBinding
import com.example.submissionakhirintermediate.ui.viewmodel.MapsViewModel
import com.example.submissionakhirintermediate.utility.ViewModelFactory
import com.example.submissionakhirintermediate.utility.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mapsViewModel: MapsViewModel
    private val boundBuilder = LatLngBounds.Builder()
    private var iteratorPage: Int = 0
    private var isStoryFirst: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        iteratorPage = 1
        supportActionBar?.title = resources.getString(R.string.map_page_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setupStory()
        myLocation()
    }

    private fun setupViewModel() {
        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mapsViewModel = ViewModelProvider(this, viewModelFactory)[MapsViewModel::class.java]
    }

    private fun setupStory() {
        mapsViewModel.getUser().observe(this) {story ->
            val token = "Bearer " + story.token
            mapsViewModel.getLocationStory(token).observe(this) {
                when(it) {
                    is Result.Loading -> {}
                    is Result.Success -> showMarker(it.data.listStory)
                    is Result.Error -> Toast.makeText(this, "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMarker(data: List<StoryApp>) {
        data.forEach {
            val latLng = it.lon?.let { it1 -> it.lat?.let { it2 -> LatLng(it2, it1) } }
            val marker = latLng?.let { it1 ->
                MarkerOptions()
                    .position(it1)
                    .title("Lokasi dari "+it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .alpha(0.7f)
                    .snippet(it.description)
            }?.let { it2 ->
                mMap.addMarker(
                    it2
                )
            }
            latLng?.let { it1 -> CameraUpdateFactory.newLatLng(it1) }
                ?.let { it2 -> mMap.moveCamera(it2) }
            if (latLng != null) {
                boundBuilder.include(latLng)
            }
            marker?.tag = it
            mMap.setOnInfoWindowClickListener {
                val intent = Intent(this, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.EXTRA_DETAIL, it.tag as StoryApp)
                }
                startActivity(intent)
            }
            if (!isStoryFirst) {
                latLng?.let { it1 -> CameraUpdateFactory.newLatLng(it1) }
                    ?.let { it2 -> mMap.moveCamera(it2) }
                isStoryFirst = true
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                myLocation()
            }
        }

    private fun myLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}