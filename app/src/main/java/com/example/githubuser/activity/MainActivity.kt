package com.example.githubuser.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar()
    }

    private fun setActionBar() {
        supportActionBar!!.elevation = 0.0F
    }
}