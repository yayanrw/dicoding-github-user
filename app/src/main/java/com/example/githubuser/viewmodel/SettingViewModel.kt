package com.example.githubuser.viewmodel

import androidx.lifecycle.*
import com.example.githubuser.core.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreferences?) : ViewModel() {
    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    fun getThemeSettings(): LiveData<Boolean> {
        return pref!!.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        _isDarkMode.value = isDarkModeActive
        viewModelScope.launch {
            pref!!.saveThemeSetting(isDarkModeActive)
        }
    }
}