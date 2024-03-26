package com.example.weatherapppoject.database

import android.util.Log
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.Alert
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInte {
    suspend fun setFavoriteData (favorite: WeatherResponse,longitude: Double,latitude: Double)
    suspend fun deleteFavData (weatherData: WeatherResponse)
     fun displayAllFav(): Flow<List<WeatherResponse>>
     fun getCityData(longitude: Double, latitude: Double): Flow<WeatherResponse>
     suspend fun insertHomeData(weatherData: WeatherResponse,longitude: Double, latitude: Double)
    fun getCityDataHome ():  Flow<WeatherResponse>
    suspend fun deleteHomeData()
     //================ALerts

    suspend fun setALertData (alert: AlertData,longitude: Double,latitude: Double)
    suspend fun deleteAlertData (weatherAlertedData: AlertData)
    fun displayAllAlerts(): Flow<List<AlertData>>
    suspend fun insertAlert(alert: AlertData)


}