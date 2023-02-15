package com.example.submissionakhirintermediate.ui.adapter.paging

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submissionakhirintermediate.R
import com.example.submissionakhirintermediate.data.model.StoryApp
import com.example.submissionakhirintermediate.databinding.ItemStoryBinding
import com.example.submissionakhirintermediate.ui.pages.DetailStoryActivity
import com.example.submissionakhirintermediate.utility.Date
import java.util.TimeZone

class StoryAdapter: PagingDataAdapter<StoryApp, StoryAdapter.StoryViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.StoryViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryAdapter.StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        internal fun bind(story: StoryApp) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .apply(RequestOptions().override(70, 70))
                    .into(binding.ivStory)
                tvName.text = story.name
                tvDescription.text = story.description

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    binding.tvCreatedTime.text = root.resources.getString(
                        R.string.created_add,
                        Date.dateFormat(story.createdAt, TimeZone.getDefault().id)
                    )
                }
            }

            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.EXTRA_DETAIL, story)
                }

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.tvName, "name"),
                        Pair(binding.tvDescription, "desc")
                    )
                it.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<StoryApp>() {
            override fun areItemsTheSame(oldItem: StoryApp, newItem: StoryApp): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryApp, newItem: StoryApp): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}