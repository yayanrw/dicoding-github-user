package com.example.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteUsers::class], version = 1)
abstract class FavoriteUsersRoomDatabase : RoomDatabase() {
    abstract fun favoriteUsersDao(): FavoriteUsersDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUsersRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteUsersRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteUsersRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteUsersRoomDatabase::class.java, "github_user_database"
                    )
                        .build()
                }
            }
            return INSTANCE as FavoriteUsersRoomDatabase
        }
    }
}