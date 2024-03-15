package com.example.weatherapppoject.repository

import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.forecastmodel.WeatherResponse

interface WeatherRepositoryInter {
    suspend fun getCurrentWeather(): WeatherList
    suspend fun getFiveDaysWeather(): WeatherResponse

}