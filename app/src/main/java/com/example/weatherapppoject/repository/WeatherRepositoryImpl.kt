package com.example.weatherapppoject.repository

import android.util.Log
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

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

//    override suspend fun getCurrentWeather(
//        latitude: Double,
//        longitude: Double,
//        units: String,
//        apiKey: String
//    ): Flow<WeatherList> {
//        return try {
//            remoteDataSource.getWeatherINfo(latitude,longitude,units,apiKey)
//        } catch (e: Exception) {
//            Log.i("===Fai Loding", "FAIL to load: Network")
//            flowOf(WeatherList()) // Return an empty WeatherList object
//        }
//    }

    override suspend fun getFiveDaysWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<WeatherResponse> {
        Log.i("=====23d", "HI: ")
        return  try {
            remoteDataSource.getFiveDaysInfo(latitude, longitude, units, apiKey, lang)
        }catch (e: Exception){
            Log.i("====Error", "getFiveDaysWeather error: "+e)
           emptyFlow()
        }

    }
}