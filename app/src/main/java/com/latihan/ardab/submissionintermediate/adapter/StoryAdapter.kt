package com.latihan.ardab.submissionintermediate.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihan.ardab.submissionintermediate.data.response.ListStoryItem
import com.latihan.ardab.submissionintermediate.databinding.ItemStoryBinding
import com.latihan.ardab.submissionintermediate.ui.story.DetailStoryActivity
import com.latihan.ardab.submissionintermediate.utils.withDateFormat

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolderStory>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: MyViewHolderStory, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderStory {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolderStory(binding)
    }

    class MyViewHolderStory(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ListStoryItem) {
            binding.apply {
                Glide.with(root.context)
                    .load(data.photoUrl)
                    .into(imgItemPhoto)

                tvItemName.text = data.name
                tvItemCreated.text = data.createdAt.withDateFormat()
                tvItemDescription.text = data.description

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                        putExtra(DetailStoryActivity.NAME, data.name)
                        putExtra(DetailStoryActivity.CREATE_AT, data.createdAt)
                        putExtra(DetailStoryActivity.DESCRIPTION, data.description)
                        putExtra(DetailStoryActivity.PHOTO_URL, data.photoUrl)
                        putExtra(DetailStoryActivity.LONGITUDE, data.lon.toString())
                        putExtra(DetailStoryActivity.LATITUDE, data.lat.toString())
                    }

                    val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair(imgItemPhoto, "photo"),
                        androidx.core.util.Pair(tvItemName, "name"),
                        androidx.core.util.Pair(tvItemCreated, "createdate"),
                        androidx.core.util.Pair(tvItemDescription, "description"),
                    )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}