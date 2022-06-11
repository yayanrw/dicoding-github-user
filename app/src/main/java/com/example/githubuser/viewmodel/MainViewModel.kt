package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.core.ApiConfig
import com.example.githubuser.model.UserSearchResponse
import com.example.githubuser.model.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    private val _users = MutableLiveData<ArrayList<UsersResponse>?>()
    val users: LiveData<ArrayList<UsersResponse>?> = _users

    init {
        getUsers()
    }

    fun getUsers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().fetchUsers()
        client.enqueue(object : Callback<ArrayList<UsersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UsersResponse>>,
                response: Response<ArrayList<UsersResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _users.value = responseBody
                    }
                } else {
                    _errorMsg.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<UsersResponse>>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserSearch(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().fetchUserSearch(query)

        client.enqueue(object : Callback<UserSearchResponse?> {
            override fun onResponse(
                call: Call<UserSearchResponse?>,
                response: Response<UserSearchResponse?>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _users.value = responseBody.items!!
                    }
                } else {
                    _errorMsg.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserSearchResponse?>, t: Throwable) {
                _isLoading.value = false
                _errorMsg.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}