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
    override suspend fun getFiveDaysInfo(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<WeatherResponse> {
        val apiKey = Utils.APIKEY

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