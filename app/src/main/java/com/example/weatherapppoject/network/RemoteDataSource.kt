package com.example.weatherapppoject.network

import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.forecastmodel.WeatherResponse


interface RemoteDataSource {
    suspend fun getWeatherINfo(): WeatherList
    suspend fun getFiveDaysInfo(): WeatherResponse
}