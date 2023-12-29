package com.latihan.ardab.submissionintermediate.apinetwork

import android.content.Context
import com.latihan.ardab.submissionintermediate.data.UserPreferenceDataStore
import com.latihan.ardab.submissionintermediate.data.database.StoryDatabase
import com.latihan.ardab.submissionintermediate.data.remote.RemoteData
import com.latihan.ardab.submissionintermediate.data.remote.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val userPreferenceDatastore = UserPreferenceDataStore.getInstance(context)
        val remoteDataSource = RemoteData.getInstance()
        val storyRoomDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, userPreferenceDatastore, remoteDataSource, storyRoomDatabase)
    }
}