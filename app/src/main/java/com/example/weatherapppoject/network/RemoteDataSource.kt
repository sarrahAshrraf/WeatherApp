package com.example.weatherapppoject.network

import com.example.weatherapppoject.pojo.WeatherResponse

interface RemoteDataSource {
    suspend fun getWeatherINfo(): WeatherResponse
}