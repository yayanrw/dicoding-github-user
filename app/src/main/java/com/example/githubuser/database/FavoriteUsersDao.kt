package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUsers: FavoriteUsers)

    @Update
    fun update(favoriteUsers: FavoriteUsers)

    @Delete
    fun delete(favoriteUsers: FavoriteUsers)

    @Query("SELECT * FROM favorite_users ORDER BY login ASC")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUsers>>

    @Query("SELECT * FROM favorite_users WHERE login = :login")
    fun getUser(login: String): LiveData<List<FavoriteUsers>>
}