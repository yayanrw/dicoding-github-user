package com.example.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubuser.database.FavoriteUsers
import com.example.githubuser.repository.FavoriteUsersRepository

class FavoriteUsersAddUpdateViewModel(application: Application) : ViewModel() {
    private val mFavoriteUsersRepository: FavoriteUsersRepository =
        FavoriteUsersRepository(application)

    fun insert(favoriteUsers: FavoriteUsers) {
        mFavoriteUsersRepository.insert(favoriteUsers)
    }

    fun delete(favoriteUsers: FavoriteUsers) {
        mFavoriteUsersRepository.delete(favoriteUsers)
    }
}