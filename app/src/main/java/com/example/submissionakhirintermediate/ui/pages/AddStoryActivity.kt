package com.example.submissionakhirintermediate.ui.pages

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.submissionakhirintermediate.R
import com.example.submissionakhirintermediate.databinding.ActivityAddStoryBinding
import com.example.submissionakhirintermediate.ui.viewmodel.AddStoryViewModel
import com.example.submissionakhirintermediate.utility.ViewModelFactory
import com.example.submissionakhirintermediate.utility.reduceFileImage
import com.example.submissionakhirintermediate.utility.rotateBitmap
import com.example.submissionakhirintermediate.utility.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Suppress("DEPRECATION")
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel
    private var getFile: File? = null
    private var result: Bitmap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double? = null
    private var lon: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.add_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        playAnimation()
        setupViewModel()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        actionButton()

    }

    private fun actionButton() {
        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnCamera.setOnClickListener {
            if (!allPermissionGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                startCameraX()
            }
        }

        binding.btnUpload.setOnClickListener {
            uploadStory()
        }

        binding.switchLocation.setOnClickListener {
            myLocation()
        }
    }

    private fun uploadStory() {
        addStoryViewModel.getUser().observe(this@AddStoryActivity) {
            val token = "Bearer " + it.token
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                val description = "${binding.edtDescription.text}".toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                addStoryViewModel.getUser().observe(this) {
                    addStoryViewModel.storyAdd(token, imageMultipart, description, lat, lon)
                    showLoading(true)
                }

                Toast.makeText(this, resources.getString(R.string.upload_success), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AddStoryActivity, MainActivity::class.java))
                finish()
            } else {
                showLoading(false)
                Toast.makeText(this, resources.getString(R.string.upload_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser =  Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun setupViewModel() {
        val viewModelFactory = ViewModelFactory.getInstance(binding.root.context)
        addStoryViewModel = ViewModelProvider(this, viewModelFactory)[AddStoryViewModel::class.java]
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.no_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (allPermissionGranted()) {
                startCameraX()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { accessLoc: Boolean ->
            if (accessLoc) {
                myLocation()
            } else {
                Toast.makeText(this, resources.getString(R.string.turn_on_location), Toast.LENGTH_SHORT).show()
            }
        }

    private fun myLocation() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                    Toast.makeText(this, resources.getString(R.string.save_locations), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, resources.getString(R.string.turn_on_location), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            (Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun playAnimation() {
        val cvImage = ObjectAnimator.ofFloat(binding.cvImg, View.ALPHA, 1f).setDuration(500)
        val btnCamera = ObjectAnimator.ofFloat(binding.btnCamera, View.ALPHA, 1f).setDuration(500)
        val btnGallery = ObjectAnimator.ofFloat(binding.btnGallery, View.ALPHA, 1f).setDuration(500)
        val textDescription =
            ObjectAnimator.ofFloat(binding.edtDescription, View.ALPHA, 1f).setDuration(500)
        val inputDescription =
            ObjectAnimator.ofFloat(binding.tfDescription, View.ALPHA, 1f).setDuration(500)
        val switchLoc =  ObjectAnimator.ofFloat(binding.switchLocation, View.ALPHA, 1f).setDuration(500)
        val btnUpload = ObjectAnimator.ofFloat(binding.btnUpload, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                cvImage,
                btnCamera,
                btnGallery,
                textDescription,
                inputDescription,
                switchLoc,
                btnUpload
            )
            start()
        }

    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}