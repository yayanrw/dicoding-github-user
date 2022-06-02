package com.example.githubuser.activity

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.core.ApiConfig
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.model.UserDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val args: DetailActivityArgs by navArgs()
        setActionBar(args.login)
        getUserDetail(args.login)
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

    private fun getUserDetail(login: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().fetchUserDetail(login)
        client.enqueue(object : Callback<UserDetailResponse?> {
            override fun onResponse(
                call: Call<UserDetailResponse?>,
                response: Response<UserDetailResponse?>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUi(responseBody)
                    }
                } else {
                    showToast(response.message())
                }
            }

            override fun onFailure(call: Call<UserDetailResponse?>, t: Throwable) {
                showToast(t.message.toString())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setActionBar(login: String) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.midnight_blue_800, theme)))
            title = login
            elevation = 0.0F
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setUi(responseBody: UserDetailResponse) {
        binding.tvLocation.text = responseBody.location
        binding.tvName.text = responseBody.name
        Glide.with(this@DetailActivity)
            .load(responseBody.avatarUrl)
            .circleCrop()
            .into(binding.imgAvatar)
        binding.tvCountPublicRepos.text = responseBody.publicRepos.toString()
        binding.tvCountFollowers.text = responseBody.followers.toString()
        binding.tvCountFollowing.text = responseBody.following.toString()
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
}