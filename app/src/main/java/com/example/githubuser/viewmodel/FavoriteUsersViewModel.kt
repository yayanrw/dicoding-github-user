package com.example.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.database.FavoriteUsers
import com.example.githubuser.repository.FavoriteUsersRepository

class FavoriteUsersViewModel(application: Application) : ViewModel() {
    private val mFavoriteUsersRepository : FavoriteUsersRepository = FavoriteUsersRepository(application)

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUsers>> = mFavoriteUsersRepository.getAllFavoriteUsers()
}