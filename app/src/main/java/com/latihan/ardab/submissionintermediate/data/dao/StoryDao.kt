package com.latihan.ardab.submissionintermediate.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.latihan.ardab.submissionintermediate.data.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(quote: List<ListStoryItem>)

    @Query("DELETE FROM tbl_story")
    suspend fun deleteAllStory()

    @Query("SELECT * FROM tbl_story")
    fun getAllStory(): PagingSource<Int, ListStoryItem>
}