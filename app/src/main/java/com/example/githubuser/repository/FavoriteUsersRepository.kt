package com.example.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.database.FavoriteUsers
import com.example.githubuser.database.FavoriteUsersDao
import com.example.githubuser.database.FavoriteUsersRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUsersRepository(application: Application) {
    private val mFavoriteUsersDao: FavoriteUsersDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUsersRoomDatabase.getDatabase(application)
        mFavoriteUsersDao = db.favoriteUsersDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUsers>> =
        mFavoriteUsersDao.getAllFavoriteUsers()

    fun insert(favoriteUsers: FavoriteUsers) {
        executorService.execute { mFavoriteUsersDao.insert(favoriteUsers) }
    }

    fun delete(favoriteUsers: FavoriteUsers) {
        executorService.execute { mFavoriteUsersDao.delete(favoriteUsers) }
    }

    fun getUser(login: String) : LiveData<List<FavoriteUsers>> = mFavoriteUsersDao.getUser(login)
}