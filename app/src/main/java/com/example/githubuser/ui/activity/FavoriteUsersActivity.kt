package com.example.githubuser.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityFavoriteUsersBinding

class FavoriteUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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