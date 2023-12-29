package com.latihan.ardab.submissionintermediate.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_remote_key")
data class RemoteKey(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)