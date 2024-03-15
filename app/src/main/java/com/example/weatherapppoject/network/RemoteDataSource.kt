package com.example.weatherapppoject.network

import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.forecastmodel.WeatherResponse


interface RemoteDataSource {
    suspend fun getWeatherINfo(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String
    ): WeatherList
    suspend fun getFiveDaysInfo(): WeatherResponse
}