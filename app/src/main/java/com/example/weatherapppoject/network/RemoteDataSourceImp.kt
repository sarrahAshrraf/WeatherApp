package com.example.weatherapppoject.network

import com.example.weatherapppoject.pojo.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceImp private constructor() : RemoteDataSource {
    private val wetherAPi: APIService

    private val BASE_URL = "https://api.openweathermap.org/data/3.0/"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        wetherAPi = retrofit.create(APIService::class.java)
    }

    companion object {
        private var instance: RemoteDataSourceImp? = null

        fun getInstance(): RemoteDataSourceImp {
            if (instance == null) {
                instance = RemoteDataSourceImp()
            }
            return instance!!
        }
    }

    override suspend fun getWeatherINfo(): WeatherResponse {
        return wetherAPi.getWeatherInfo()
        //TODO Pass Paramter
    }

}


