package com.example.capstone.data.retrofit

import com.example.capstone.data.response.PostResponse
import com.example.capstone.data.response.Response
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServiceNews {
    @GET("news")
    suspend fun getEvents(): Response
}

interface ApiServiceDetect {
    @Multipart
    @POST("upload-image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
    ): PostResponse
}