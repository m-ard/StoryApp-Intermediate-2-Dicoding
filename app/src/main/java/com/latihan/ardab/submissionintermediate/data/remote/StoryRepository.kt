package com.latihan.ardab.submissionintermediate.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.latihan.ardab.submissionintermediate.apinetwork.ApiService
import com.latihan.ardab.submissionintermediate.data.UserPreferenceDataStore
import com.latihan.ardab.submissionintermediate.data.database.StoryDatabase
import com.latihan.ardab.submissionintermediate.data.response.ListStoryItem
import com.latihan.ardab.submissionintermediate.data.response.LoginResult
import com.latihan.ardab.submissionintermediate.data.response.LoginResponse
import com.latihan.ardab.submissionintermediate.data.response.NewStoryResponse
import com.latihan.ardab.submissionintermediate.data.response.SignUpResponse
import com.latihan.ardab.submissionintermediate.data.response.StoryResponse

import java.io.File

class StoryRepository(
    private val apiService: ApiService,
    private val pref: UserPreferenceDataStore,
    private val remoteDataSource: RemoteData,
    private val storyRoomDatabase: StoryDatabase
) : AppDataSource {

    override  fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    suspend fun saveUser(userName: String, userId: String, userToken: String) {
        pref.saveUser(userName,userId,userToken)
    }

    suspend fun logout() {
        pref.logOut()
    }


    override  fun login(email: String, password: String): LiveData<LoginResponse> {
        val loginResponse2 = MutableLiveData<LoginResponse>()

        remoteDataSource.signin(object : RemoteData.SigninCallback{
            override fun onLogin(loginResponse: LoginResponse) {
                loginResponse2.postValue(loginResponse)
            }
        }, email, password)
        return loginResponse2
    }

    override  fun signup(name: String, email: String, password: String): LiveData<SignUpResponse> {
        val registerResponse = MutableLiveData<SignUpResponse>()

        remoteDataSource.signup(object : RemoteData.SignupCallback{
            override fun onSignUp(signUpResponse: SignUpResponse) {
                registerResponse.postValue(signUpResponse)
            }
        }, name, email, password)
        return registerResponse
    }

    override  fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            pagingSourceFactory = {
                StoryPaging(
                    apiServicePaging = apiService,
                    dataStoreRepository = pref
                )
            }
        ).liveData
    }

    override  fun postNewStory(token: String, imageFile: File, desc: String, lon: String?, lat: String?): LiveData<NewStoryResponse> {
        val uploadResponseStatus = MutableLiveData<NewStoryResponse>()

        remoteDataSource.postNewStory(object : RemoteData.AddNewStoryCallback{
            override fun onNewStory(newStoryResponse: NewStoryResponse) {
                uploadResponseStatus.postValue(newStoryResponse)
            }
        }, token, imageFile, desc, lon, lat)
        return uploadResponseStatus
    }

    override fun getListMapsStory(token: String): LiveData<StoryResponse> {
        val storyResponse2 = MutableLiveData<StoryResponse>()
        remoteDataSource.getListMapsStory(object: RemoteData.GetListMapsStoryCallback{
            override fun onMapsStoryLoad(storyResponse: StoryResponse) {
                storyResponse2.postValue(storyResponse)
            }

        },token)
        return storyResponse2
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: UserPreferenceDataStore,
            remoteDataSource: RemoteData,
            storyRoomDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref, remoteDataSource, storyRoomDatabase)
            }.also { instance = it }
    }

}