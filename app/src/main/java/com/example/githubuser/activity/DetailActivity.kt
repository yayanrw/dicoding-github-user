package com.example.githubuser.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.model.UserDetailResponse
import com.example.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgbOpenOnGithub.setOnClickListener(this)

        val args: DetailActivityArgs by navArgs()
        setActionBar(args.login)
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
        detailViewModel.getUserDetail(args.login)
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
    private fun setActionBar(login: String) {
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