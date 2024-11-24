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

    fun displayEvents(){
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getEvents().enqueue(object : Callback<Response> {
            override fun onResponse(
                call: Call<Response>,
                response: retrofit2.Response<Response>
            )
            {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _events.value = response.body()?.articles?: emptyList()
                    Log.d("API_RESPONSE", response.body()?.articles.toString())
                }else {
                    _isLoading.value = false
                    Log.e("API_ERROR", "Response failed: ${response.code()}")
                }

            }
            override fun onFailure(call: Call<Response>, t: Throwable) {
                _isLoading.value = false
                Log.d("API_ERROR", t.message.toString())
            }
        })
    }
}