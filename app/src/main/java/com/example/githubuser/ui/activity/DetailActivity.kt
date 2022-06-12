package com.example.githubuser.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.core.SettingPreferences
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.model.UserDetailResponse
import com.example.githubuser.viewmodel.DetailViewModel
import com.example.githubuser.viewmodel.SettingViewModel
import com.example.githubuser.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var settingViewModel: SettingViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgbOpenOnGithub.setOnClickListener(this)

        val args: DetailActivityArgs by navArgs()
        setViewPager()

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailViewModel.errorMsg.observe(this) {
            showToast(it)
        }
        detailViewModel.userDetail.observe(this) {
            it?.let { it1 -> setUi(it1) }
        }

        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                setActionBar(args.login, true)
            } else {
                setActionBar(args.login, false)
            }
        }

        detailViewModel.getUserDetail(args.login)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            androidx.appcompat.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setActionBar(login: String, isDarkMode: Boolean) {
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
            title = login
            elevation = 0.0F
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setViewPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setUi(responseBody: UserDetailResponse) {
        binding.tvLocation.text = responseBody.location
        binding.tvName.text = responseBody.name
        Glide.with(this@DetailActivity)
            .load(responseBody.avatarUrl)
            .circleCrop()
            .into(binding.imgAvatar)
        binding.tvBio.text = (responseBody.bio ?: getString(R.string.no_bio)).toString()

        if (responseBody.blog?.isEmpty() == true) {
            binding.tvBlog.text = getString(R.string.no_blog)
        } else {
            binding.tvBlog.text = responseBody.blog.toString()
        }

        binding.tvCompany.text = (responseBody.company ?: getString(R.string.no_company)).toString()
        binding.tvCountPublicRepos.text = responseBody.publicRepos.toString()
        binding.tvCountFollowers.text = responseBody.followers.toString()
        binding.tvCountFollowing.text = responseBody.following.toString()
        binding.tvLinkGithub.text = responseBody.htmlUrl
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.lnrContent.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.lnrContent.visibility = View.VISIBLE
        }
    }

    private fun showToast(messages: String) {
        Toast.makeText(this@DetailActivity, messages, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgb_open_on_github -> {
                val url = binding.tvLinkGithub.text.toString()
                val openGithub = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(openGithub)
            }
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}