package com.example.githubuser.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.FavoriteUsersAdapter
import com.example.githubuser.core.SettingPreferences
import com.example.githubuser.databinding.ActivityFavoriteUsersBinding
import com.example.githubuser.viewmodel.FavoriteUsersViewModel
import com.example.githubuser.viewmodel.ViewModelFactory

class FavoriteUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUsersBinding
    private lateinit var favoriteUsersAdapter: FavoriteUsersAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        setupRV()

        setActionBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setupRV() {
        favoriteUsersAdapter = FavoriteUsersAdapter()

        binding.rvGithubUsers.layoutManager = LinearLayoutManager(this)
        binding.rvGithubUsers.setHasFixedSize(true)
        binding.rvGithubUsers.adapter = favoriteUsersAdapter
    }

    private fun initViewModel() {
        val favoriteUsersViewModel = obtainViewModel(this@FavoriteUsersActivity)
        favoriteUsersViewModel.getAllFavoriteUsers().observe(this) { favoriteUsersList ->
            if (favoriteUsersList != null) {
                favoriteUsersAdapter.setListFavoriteUsers(favoriteUsersList)
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUsersViewModel {
        val pref = SettingPreferences.getInstance(dataStore)

        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(FavoriteUsersViewModel::class.java)
    }

    private fun setActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.favorite_users)
            elevation = 0.0F
        }
    }
}