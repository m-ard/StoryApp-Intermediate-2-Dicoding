package com.latihan.ardab.submissionintermediate.data.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.latihan.ardab.submissionintermediate.data.response.ListStoryItem
import com.latihan.ardab.submissionintermediate.data.response.LoginResponse
import com.latihan.ardab.submissionintermediate.data.response.LoginResult
import com.latihan.ardab.submissionintermediate.data.response.NewStoryResponse
import com.latihan.ardab.submissionintermediate.data.response.SignUpResponse
import com.latihan.ardab.submissionintermediate.data.response.StoryResponse
import java.io.File

interface AppDataSource {
    fun getUser(): LiveData<LoginResult>
    fun login(email: String, password: String): LiveData<LoginResponse>
    fun signup(name: String, email: String, password: String): LiveData<SignUpResponse>
    fun getListMapsStory(token: String): LiveData<StoryResponse>
    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>>
    fun postNewStory(token: String, imageFile: File, desc: String, lon: String?, lat: String?): LiveData<NewStoryResponse>
}