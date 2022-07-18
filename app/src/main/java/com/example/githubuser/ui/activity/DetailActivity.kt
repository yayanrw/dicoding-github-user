package com.example.githubuser.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.core.SettingPreferences
import com.example.githubuser.database.FavoriteUsers
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.model.UserDetailResponse
import com.example.githubuser.viewmodel.DetailViewModel
import com.example.githubuser.viewmodel.FavoriteUsersAddUpdateViewModel
import com.example.githubuser.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var favoriteUsersAddUpdateViewModel: FavoriteUsersAddUpdateViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var login: String
    private lateinit var type: String
    private lateinit var avatarUrl: String
    private var isFavorite = false

    private val args: DetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewPager()
        setActionBar(args.login)

        binding.imgbOpenOnGithub.setOnClickListener(this)
        binding.imgbFavorite.setOnClickListener(this)
        initViewModel(args.login)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            androidx.appcompat.R.id.home -> {
                finish()
                return true
            }
            R.id.share_action -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND

                    val text = """${binding.tvName.text}
@${args.login}

Work at ${binding.tvCompany.text}
Blog Site: ${binding.tvBlog.text}
Public Repos: ${binding.tvCountPublicRepos.text}
Followers: ${binding.tvCountFollowers.text}
Following: ${binding.tvCountFollowing.text}
"""

                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBar(login: String) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = login
            elevation = 0.0F
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgb_open_on_github -> {
                val url = binding.tvLinkGithub.text.toString()
                val openGithub = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(openGithub)
            }
            R.id.imgb_favorite -> {
                val favoriteUsers = FavoriteUsers(
                    login,
                    type,
                    avatarUrl
                )
                favoriteUsersAddUpdateViewModel.insert(favoriteUsers)
                showToast(getString(R.string.favorite_user_added, login))
            }
        }
    }

    private fun initViewModel(login: String) {
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailViewModel.errorMsg.observe(this) {
            showToast(it)
        }
        detailViewModel.userDetail.observe(this) {
            it?.let { it1 -> setUi(it1) }
        }
        detailViewModel.getUserDetail(login)
        favoriteUsersAddUpdateViewModel = obtainViewModel(this@DetailActivity)
        favoriteUsersAddUpdateViewModel.getUser(login)
            .observe(this@DetailActivity) { listFavoriteUsers ->
                isFavorite = listFavoriteUsers.isNotEmpty()

                if (isFavorite) {
                    binding.imgbFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    binding.imgbFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUsersAddUpdateViewModel {
        val pref = SettingPreferences.getInstance(dataStore)

        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(FavoriteUsersAddUpdateViewModel::class.java)
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

        login = responseBody.login.toString()
        type = responseBody.type.toString()
        avatarUrl = responseBody.avatarUrl.toString()
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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}