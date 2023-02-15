package com.example.submissionakhirintermediate.ui.pages

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionakhirintermediate.R
import com.example.submissionakhirintermediate.databinding.ActivityMainBinding
import com.example.submissionakhirintermediate.ui.adapter.paging.StoryAdapter
import com.example.submissionakhirintermediate.ui.viewmodel.LoginViewModel
import com.example.submissionakhirintermediate.ui.viewmodel.StoryViewModel
import com.example.submissionakhirintermediate.utility.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var listStoryViewModel: StoryViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Float? = null
    private var lon: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.setHasFixedSize(true)
        setupViewModel()
        storyAdapter =
            StoryAdapter()

        listStoryViewModel.getUser().observe(this) {
            if (it.isLogin) {
                getStory()
            } else {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finishAffinity()
            }
        }

        actionBtn()
    }

    private fun actionBtn() {
        binding.btnMaps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.btnAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setupViewModel() {
        viewModelFactory = ViewModelFactory.getInstance(this)

        listStoryViewModel = ViewModelProvider(this, viewModelFactory)[StoryViewModel::class.java]
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun getStory() {
        binding.rvStory.adapter = storyAdapter

        listStoryViewModel.getStory().observe(this@MainActivity) {
            storyAdapter.submitData(lifecycle, it)
            showLoading(false)
        }
    }

    private fun logout() {
        loginViewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.logout -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                myLocation()
            }
        }

    private fun myLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lat = location.latitude.toFloat()
                    lon = location.longitude.toFloat()
                    Toast.makeText(
                        this,
                        resources.getString(R.string.save_locations),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.locations_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            (Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarLoading.visibility = View.VISIBLE
        } else {
            binding.progressBarLoading.visibility = View.GONE
        }
    }

}