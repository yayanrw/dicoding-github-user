package com.example.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.core.SettingPreferences

class ViewModelFactory(
    private val mApplication: Application,
    private val pref: SettingPreferences
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(FavoriteUsersViewModel::class.java)) {
            return FavoriteUsersViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteUsersAddUpdateViewModel::class.java)) {
            return FavoriteUsersAddUpdateViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, pref: SettingPreferences): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, pref)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}