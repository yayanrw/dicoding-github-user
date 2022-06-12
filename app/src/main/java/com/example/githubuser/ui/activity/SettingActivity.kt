package com.example.githubuser.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setBackgroundDrawable(
                ColorDrawable(
                    resources.getColor(
                        R.color.midnight_blue_800,
                        theme
                    )
                )
            )
            title = getString(R.string.setting)
            elevation = 0.0F
        }
    }
}