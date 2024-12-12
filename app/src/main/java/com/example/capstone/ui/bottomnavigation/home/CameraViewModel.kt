package com.example.capstone.ui.bottomnavigation.home

import androidx.lifecycle.ViewModel
import com.example.capstone.data.response.PostResponse
import com.example.capstone.data.retrofit.ApiConfig
import com.example.capstone.data.retrofit.ApiServiceDetect
import okhttp3.MultipartBody

class CameraViewModel : ViewModel() {

    suspend fun uploadFace(body: MultipartBody.Part): PostResponse {
        return ApiConfig.getApiServiceDetect().uploadImage(body)
    }
}