package com.example.githubuser.viewmodel

import androidx.lifecycle.*
import com.example.githubuser.core.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreferences?) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref!!.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref!!.saveThemeSetting(isDarkModeActive)
        }
    }
}