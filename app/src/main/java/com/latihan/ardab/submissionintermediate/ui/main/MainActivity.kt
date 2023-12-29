package com.latihan.ardab.submissionintermediate.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.ardab.submissionintermediate.R
import com.latihan.ardab.submissionintermediate.adapter.LoadingAdapter
import com.latihan.ardab.submissionintermediate.adapter.StoryAdapter
import com.latihan.ardab.submissionintermediate.databinding.ActivityMainBinding
import com.latihan.ardab.submissionintermediate.ui.login.LoginActivity
import com.latihan.ardab.submissionintermediate.ui.maps.MapsActivity
import com.latihan.ardab.submissionintermediate.ui.story.NewStoryActivity
import com.latihan.ardab.submissionintermediate.viewmodel.LoginViewModel
import com.latihan.ardab.submissionintermediate.viewmodel.MainViewModel
import com.latihan.ardab.submissionintermediate.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var signViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.dashboard_story)

        val factory = ViewModelFactory.getInstance(this)
        signViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val adapter = StoryAdapter()

        showLoading(true)

        signViewModel.getUser().observe(this){user->
            if (user.userId.isEmpty()){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                binding.rvListStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.getStory(user.token).observe(this) {
                    Log.e("List", it.toString())
                    adapter.submitData(lifecycle, it)
                    showLoading(false)
                }
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvListStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListStory.addItemDecoration(itemDecoration)



        binding.btnAddStory.setOnClickListener {
            val i = Intent(this@MainActivity, NewStoryActivity::class.java)
            startActivity(i)
        }
        binding.btnGotoMap.setOnClickListener {
            val i = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                signViewModel.logOut()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}