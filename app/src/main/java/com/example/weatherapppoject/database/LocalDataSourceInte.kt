package com.example.weatherapppoject.database

import android.util.Log
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInte {
    suspend fun setFavoriteData (favorite: WeatherResponse,longitude: Double,latitude: Double)
    suspend fun deleteFavData (weatherData: WeatherResponse)
     fun displayAllFav(): Flow<List<WeatherResponse>>
     fun getCityData(longitude: Double, latitude: Double): Flow<WeatherResponse>

     //================ALerts

    suspend fun setALertData (alert: OneApiCall,longitude: Double,latitude: Double)
    suspend fun deleteAlertData (weatherAlertedData: OneApiCall)
    fun displayAllAlerts(): Flow<List<OneApiCall>>


}