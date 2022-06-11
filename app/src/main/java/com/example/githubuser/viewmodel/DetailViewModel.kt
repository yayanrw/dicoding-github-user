package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.core.ApiConfig
import com.example.githubuser.model.UserDetailResponse
import com.example.githubuser.model.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followersIsLoading = MutableLiveData<Boolean>()
    val followersIsLoading: LiveData<Boolean> = _followersIsLoading

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    private val _userDetail = MutableLiveData<UserDetailResponse?>()
    val userDetail: LiveData<UserDetailResponse?> = _userDetail

    private val _followers = MutableLiveData<ArrayList<UsersResponse>?>()
    val followers: LiveData<ArrayList<UsersResponse>?> = _followers

    fun getUserDetail(login: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().fetchUserDetail(login)
        client.enqueue(object : Callback<UserDetailResponse?> {
            override fun onResponse(
                call: Call<UserDetailResponse?>,
                response: Response<UserDetailResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userDetail.value = responseBody
                    }
                } else {
                    _errorMsg.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse?>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowers(login: String) {
        _followersIsLoading.value = true
        val client = ApiConfig.getApiService().fetchFollowers(login)
        client.enqueue(object : Callback<ArrayList<UsersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UsersResponse>>,
                response: Response<ArrayList<UsersResponse>>
            ) {
                _followersIsLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _followers.value = responseBody
                    }
                } else {
                    _errorMsg.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersResponse>>, t: Throwable) {
                _followersIsLoading.value = false
                _errorMsg.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }
}