package com.example.weatherapppoject.network

import com.example.weatherapppoject.forecastmodel.WeatherResponse


interface RemoteDataSource {
    suspend fun getWeatherINfo(): WeatherResponse
    suspend fun getFiveDaysInfo(): WeatherResponse
}