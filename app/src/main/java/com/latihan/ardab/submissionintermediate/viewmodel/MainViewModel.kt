package com.latihan.ardab.submissionintermediate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.latihan.ardab.submissionintermediate.data.remote.StoryRepository
import java.io.File

class MainViewModel(private val repo: StoryRepository) : ViewModel() {

    val coordLat = MutableLiveData(0.0)
    val coordLon = MutableLiveData(0.0)

    fun getStory(token: String) =  repo.getAllStory(token)
    fun uploadStory(token: String, imageFile: File, desc: String, lon: String?, lat: String?) = repo.postNewStory(token, imageFile, desc, lon, lat)
    fun getListMapsStory(token: String) = repo.getListMapsStory(token)
}