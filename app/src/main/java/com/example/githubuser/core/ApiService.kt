package com.example.githubuser.core

import com.example.githubuser.model.UsersResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    fun getUsers (): Call<UsersResponse>
}