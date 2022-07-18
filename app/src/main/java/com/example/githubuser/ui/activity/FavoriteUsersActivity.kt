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
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val adapter: FavoriteUsersAdapter by lazy {
        FavoriteUsersAdapter()
    }

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
        with(binding) {
            val layoutManager = LinearLayoutManager(this@FavoriteUsersActivity)
            this.rvGithubUsers.layoutManager = layoutManager
            this.rvGithubUsers.setHasFixedSize(true)
            this.rvGithubUsers.adapter = adapter
        }
    }

    private fun initViewModel() {
        val favoriteUsersViewModel = obtainViewModel(this@FavoriteUsersActivity)
        favoriteUsersViewModel.getAllFavoriteUsers().observe(
            this@FavoriteUsersActivity
        ) { favList ->
            if (favList != null) {
                adapter.setListFavorite(favList)
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUsersViewModel {
        val pref = SettingPreferences.getInstance(dataStore)

        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[FavoriteUsersViewModel::class.java]
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