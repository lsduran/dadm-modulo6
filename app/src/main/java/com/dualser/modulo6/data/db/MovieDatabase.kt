package com.dualser.modulo6.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dualser.modulo6.data.db.model.MovieEntity
import com.dualser.modulo6.utils.Constants

@Database(
    entities = [MovieEntity::class],
    version = 3,
    exportSchema = true
)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder( // Singleton
                    context.applicationContext,
                    MovieDatabase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE= instance
                instance
            }
        }

    }
}