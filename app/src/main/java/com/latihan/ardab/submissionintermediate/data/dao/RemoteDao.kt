package com.latihan.ardab.submissionintermediate.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RemoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<RemoteKey>)
    @Query("SELECT * FROM table_remote_key WHERE id = :id")
    fun getRemoteKeysId(id: String): RemoteKey?
    @Query("DELETE FROM table_remote_key")
    fun deleteRemoteKeys()
}