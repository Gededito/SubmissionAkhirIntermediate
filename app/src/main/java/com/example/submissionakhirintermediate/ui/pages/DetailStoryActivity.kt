package com.example.submissionakhirintermediate.ui.pages

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.submissionakhirintermediate.R
import com.example.submissionakhirintermediate.data.model.StoryApp
import com.example.submissionakhirintermediate.databinding.ActivityDetailStoryBinding
import com.example.submissionakhirintermediate.utility.Date
import java.util.*

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        displayResult()
    }

    private fun displayResult() {
        val data = intent.getParcelableExtra<StoryApp>(EXTRA_DETAIL)
        binding.apply {
            Glide.with(ivStory)
                .load(data?.photoUrl)
                .placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_error_image)
                .into(ivStory)

            tvName.text = data?.name
            tvDescription.text = data?.description

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.tvCreatedTime.text = root.resources.getString(
                    R.string.created_add,
                    Date.dateFormat(data?.createdAt, TimeZone.getDefault().id)
                )
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

}