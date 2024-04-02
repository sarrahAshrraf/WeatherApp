package com.example.weatherapppoject.repository

import android.util.Log
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn

class WeatherRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
     private val localDataSource: LocalDataSourceInte
) : WeatherRepositoryInter {

    companion object {
        private var instance: WeatherRepositoryImpl? = null

        fun getInstance(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSourceInte): WeatherRepositoryImpl {
            return instance ?: synchronized(this) {
                val temp = WeatherRepositoryImpl(remoteDataSource,localDataSource)
                instance = temp
                temp
            }
        }
    }


    override suspend fun getFiveDaysWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<WeatherResponse> {
        return  try {
            remoteDataSource.getFiveDaysInfo(latitude, longitude, units, apiKey, lang)
        }catch (e: Exception){
            Log.i("====Error", "getFiveDaysWeather error: "+e)
           emptyFlow()
        }

    }

    override suspend fun getAlertData(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<OneApiCall> {
        return  try {
            remoteDataSource.getALerts(latitude, longitude, units, apiKey, lang)
        }catch (e: Exception){
            emptyFlow()
        }

    }



    //Data Base functions
    override fun getFavoriteData(): Flow<List<WeatherResponse>> {
      return  localDataSource.displayAllFav()
    }

    override suspend fun insertfavIntoDB(
        fav: WeatherResponse,
        longitude: Double,
        latitude: Double
    ) {
        localDataSource.setFavoriteData(fav,longitude,latitude)
    }

    override suspend fun deleteFromFav(weatherData: WeatherResponse) {
        localDataSource.deleteFavData(weatherData)
    }
    override suspend fun deleteFromHome(weatherData: WeatherResponse) {
        localDataSource.deleteHomeData(weatherData)
    }

    override fun getFavCityInfo(longitude: Double, latitude: Double) :Flow<WeatherResponse> {
        return localDataSource.getCityData(longitude,latitude)
    }

    override suspend fun insertHomeData(weatherData: WeatherResponse, longitude: Double, latitude: Double) {
        return localDataSource.insertHomeData(weatherData, longitude,latitude)
    }

    override fun getFavCityInfoHome(): Flow<WeatherResponse> {
        return localDataSource.getCityDataHome()
    }

    override suspend fun deleteHome() {
        localDataSource.deleteHomeData()
    }

    override fun getAlertedData(): Flow<List<AlertData>> {
        return  localDataSource.displayAllAlerts()
    }

    override suspend fun insertAlertIntoDB(
        alerts: AlertData,
        longitude: Double,
        latitude: Double
    ) {
        localDataSource.setALertData(alerts,longitude,latitude)
    }

    override suspend fun insertAlerts(alert: AlertData) {
        localDataSource.insertAlert(alert)
    }

    override suspend fun deleteFromAlerts(alertWeatherData: AlertData) {
        localDataSource.deleteAlertData(alertWeatherData)
    }

}