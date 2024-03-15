package com.example.weatherapppoject.repository

import android.util.Log
import com.example.WeatherAppProject.Weather
import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.forecastmodel.City
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.network.RemoteDataSource

class WeatherRepositoryImpl private constructor(
    private val remoteDataSource: RemoteDataSource
    // private val localDataSource: LocalDataSource
) : WeatherRepositoryInter {

    companion object {
        private var instance: WeatherRepositoryImpl? = null

        fun getInstance(remoteDataSource: RemoteDataSource): WeatherRepositoryImpl {
            return instance ?: synchronized(this) {
                val temp = WeatherRepositoryImpl(remoteDataSource)
                instance = temp
                temp
            }
        }
    }

    override suspend fun getCurrentWeather(): WeatherList {
        return try {
            remoteDataSource.getWeatherINfo()
        } catch (e: Exception) {
            Log.i("===Fai Loding", "FAIL to load: Network")
            WeatherList() // Return an empty WeatherList object
        }
    }

    override suspend fun getFiveDaysWeather(): WeatherResponse {
        Log.i("=====23d", "HI: ")
        return  remoteDataSource.getFiveDaysInfo()


    }
}