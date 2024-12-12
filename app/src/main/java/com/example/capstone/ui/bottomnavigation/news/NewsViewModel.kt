package com.example.capstone.ui.bottomnavigation.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.data.response.ArticlesItem
import com.example.capstone.data.response.Response
import com.example.capstone.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class NewsViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ArticlesItem>>()
    val events: LiveData<List<ArticlesItem>> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun displayEvents() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiServiceNews()
        try {
            val response = apiService.getEvents()
            _isLoading.value = false
            if (response.status == "ok") {
                _events.value = response.articles
            } else {
                Log.e("API_ERROR", "Response failed: ${response.status}")
            }
        } catch (e: Exception) {
            _isLoading.value = false
            Log.e("API_ERROR", "Exception: ${e.message}")
        }
    }

}