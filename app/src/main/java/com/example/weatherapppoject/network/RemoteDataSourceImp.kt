package com.example.weatherapppoject.network

import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import com.example.weatherapppoject.utils.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Utils.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherApiService: APIService = retrofit.create(APIService::class.java)
}

class RemoteDataSourceImp : RemoteDataSource {
    private val weatherApiService = RetrofitInstance.weatherApiService

//    override suspend fun getWeatherINfo(
//        latitude: Double,
//        longitude: Double,
//        units: String,
//        apiKey: String
//    ): Flow<WeatherList> {
//        val city = "new york" // Replace with the desired city
//        val units = "metric" // Replace with the desired units
//        val apiKey = Utils.APIKEY // Replace with your API key
//
////        return weatherApiService.getCureentWeather(51.5085,-0.1257, units, apiKey)
//        return flowOf( weatherApiService.getCureentWeather(latitude,longitude,units,apiKey))
//    }

    override suspend fun getFiveDaysInfo(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<WeatherResponse> {
        val city = "new york" // Replace with the desired city
        val units = "metric" // Replace with the desired units
        val apiKey = Utils.APIKEY // Replace with your API key
//        val lang = "ar" // Replace with the desired language

        return flowOf( weatherApiService.getForeCast(latitude, longitude , units, apiKey, lang))
    }

    override suspend fun getALerts(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<OneApiCall> {
        return flowOf( weatherApiService.getAlerts(latitude, longitude , units, apiKey, lang))
    }
}