package com.example.weatherapppoject.repository

import android.util.Log
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class WeatherRepositoryImpl private constructor(
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

    override suspend fun getAlertData(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<OneApiCall> {
        Log.i("=====23d", "HI: ")
        return  try {
            remoteDataSource.getALerts(latitude, longitude, units, apiKey, lang)
        }catch (e: Exception){
            Log.i("====Error", "get alert in repo error: "+e)
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

    override fun getFavCityInfo(longitude: Double, latitude: Double) :Flow<WeatherResponse> {
        return localDataSource.getCityData(longitude,latitude)
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