package com.latihan.ardab.submissionintermediate.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.latihan.ardab.submissionintermediate.data.dao.RemoteDao
import com.latihan.ardab.submissionintermediate.data.dao.RemoteKey
import com.latihan.ardab.submissionintermediate.data.dao.StoryDao
import com.latihan.ardab.submissionintermediate.data.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, RemoteKey::class],
    version = 4,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeyDao(): RemoteDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java,
                    "db_stories"
                ).fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}