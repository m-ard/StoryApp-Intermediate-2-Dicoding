package com.latihan.ardab.submissionintermediate.ui.story

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.latihan.ardab.submissionintermediate.R
import com.latihan.ardab.submissionintermediate.databinding.ActivityDetailStoryBinding
import com.latihan.ardab.submissionintermediate.utils.getAddressName
import com.latihan.ardab.submissionintermediate.utils.withDateFormat

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityDetailStoryBinding

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            supportActionBar?.title = getString(R.string.detail_story)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            val photoUrl = intent.getStringExtra(PHOTO_URL)
            val name = intent.getStringExtra(NAME)
            val createAt = intent.getStringExtra(CREATE_AT)
            val description = intent.getStringExtra(DESCRIPTION)
            val lon = intent.getStringExtra(LONGITUDE)!!.toDouble()
            val lat = intent.getStringExtra(LATITUDE)!!.toDouble()
            val location = getAddressName(this@DetailStoryActivity, lat, lon)

            Glide.with(root.context)
                .load(photoUrl)
                .into(ivDetailPhoto)
            tvDetailName.text = name
            tvDetailCreatedTime.text = createAt?.withDateFormat()
            tvDetailDescription.text = description
            tvDetailLocation.text = location

            ivLocation.visibility = if (lon == 0.0 && lat == 0.0) View.INVISIBLE else View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    companion object {
        const val NAME = "name"
        const val CREATE_AT = "create at"
        const val DESCRIPTION = "description"
        const val PHOTO_URL = "photoUrl"
        const val LONGITUDE = "lon"
        const val LATITUDE = "lat"
    }
}

