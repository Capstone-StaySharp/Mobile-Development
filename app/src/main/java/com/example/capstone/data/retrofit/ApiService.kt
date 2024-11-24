package com.example.capstone.data.retrofit

import com.example.capstone.data.response.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("news")
    fun getEvents(): Call<Response>
}