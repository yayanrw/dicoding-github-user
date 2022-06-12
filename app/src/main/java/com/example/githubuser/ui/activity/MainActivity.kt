package com.example.githubuser.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.R

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setActionBar() {
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.midnight_blue_800, theme)))
            elevation = 0.0F
        }
    }
}