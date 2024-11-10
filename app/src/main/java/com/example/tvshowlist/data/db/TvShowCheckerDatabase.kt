package com.example.tvshowlist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tvshowlist.domain.model.TvShow
import com.example.tvshowlist.domain.model.TvShowSeasonEpisodes

@Database(entities = [TvShow::class, TvShowSeasonEpisodes::class], version = 2, exportSchema = false)
abstract class TvShowCheckerDatabase : RoomDatabase() {
    abstract fun tvShowCheckerDao(): TvShowCheckerDao

    companion object {
        @Volatile
        private var INSTANCE: TvShowCheckerDatabase? = null

        fun getDatabase(context: Context): TvShowCheckerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TvShowCheckerDatabase::class.java,
                    "tv_show_checker_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                return instance
            }
        }
    }
}