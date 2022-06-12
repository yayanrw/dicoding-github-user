package com.example.githubuser.ui.activity

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.core.SettingPreferences
import com.example.githubuser.databinding.ActivityFavoriteUsersBinding
import com.example.githubuser.viewmodel.SettingViewModel
import com.example.githubuser.viewmodel.ViewModelFactory

class FavoriteUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUsersBinding
    private lateinit var settingViewModel: SettingViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                setActionBar(true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                setActionBar(false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setActionBar(isDarkMode: Boolean) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            if (isDarkMode) {
                setBackgroundDrawable(
                    ColorDrawable(
                        resources.getColor(
                            R.color.midnight_blue_800,
                            theme
                        )
                    )
                )
            } else {
                setBackgroundDrawable(
                    ColorDrawable(
                        resources.getColor(
                            R.color.amethyst_700,
                            theme
                        )
                    )
                )
            }
            title = getString(R.string.favorite_users)
            elevation = 0.0F
        }
    }
}