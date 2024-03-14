package com.example.weatherapppoject.network

import com.example.weatherapppoject.utils.Utils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




object RetrofitInstance {
    val wetherAPi: APIService by lazy {
        Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}
