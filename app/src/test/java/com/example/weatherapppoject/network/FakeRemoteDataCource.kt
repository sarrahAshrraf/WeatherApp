package com.example.weatherapppoject.network

import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow

class FakeRemoteDataCource : RemoteDataSource {
    override suspend fun getFiveDaysInfo(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getALerts(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<OneApiCall> {
        TODO("Not yet implemented")
    }
}