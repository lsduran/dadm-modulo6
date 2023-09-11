package com.dualser.modulo6.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dualser.modulo6.utils.Constants
import com.dualser.modulo6.utils.PlatformEnum

@Entity(tableName = Constants.DATABASE_MOVIES_TABLE)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id")
    val id: Long = 0,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "genre")
    var genre: String,

    @ColumnInfo(name = "platform")
    var platform: String,

    @ColumnInfo(name = "platform_image")
    var platformImage: PlatformEnum,

    @ColumnInfo(name = "rating")
    var rating: Float
)
