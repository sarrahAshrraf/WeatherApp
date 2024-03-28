package com.example.weatherapppoject.data

import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataCourceImp (var homeAndfav : MutableList<WeatherResponse> = mutableListOf(),
                              val oneapi : MutableList<OneApiCall> = mutableListOf(),
                              val alert : MutableList<AlertData> = mutableListOf() )
        : LocalDataSourceInte {




    override suspend fun setFavoriteData(
        favorite: WeatherResponse,
        longitude: Double,
        latitude: Double
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavData(weatherData: WeatherResponse) {
        TODO("Not yet implemented")
    }

    override fun displayAllFav(): Flow<List<WeatherResponse>> {
        TODO("Not yet implemented")
    }

    override fun getCityData(longitude: Double, latitude: Double): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertHomeData(
        weatherData: WeatherResponse,
        longitude: Double,
        latitude: Double
    ) {
        TODO("Not yet implemented")
    }

    override fun getCityDataHome(): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHomeData() {
        TODO("Not yet implemented")
    }

    override suspend fun setALertData(alert: AlertData, longitude: Double, latitude: Double) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertData(weatherAlertedData: AlertData) {
        TODO("Not yet implemented")
    }

    override fun displayAllAlerts(): Flow<List<AlertData>>  = alert.let{
        return@let flowOf(it)
    }

    override suspend fun insertAlert(dAlert: AlertData) {
        alert.add(dAlert)
    }


}


//override suspend fun insertHomeWeather(entityHome: EntityHome) {
//            home.removeFirst()
//            home.add(entityHome)
//        }
//
//        override val getHomeWeather: Flow<EntityHome> = home.let {
//            return@let it.asFlow()
//        }
//
//        override val getFavorite: Flow<List<EntityFavorite>> = favorite.let {
//            return@let flowOf(it)
//        }
//
//        override suspend fun insertFavorite(entityFavorite: EntityFavorite) {
//            favorite.add(entityFavorite)
//        }
//
//        override suspend fun deleteFavorite(entityFavorite: EntityFavorite) {
//            favorite.remove(entityFavorite)
//        }
//
//        override val getAlert: Flow<List<EntityAlert>> = alert.let {
//            return@let flowOf(it)
//        }
//
//        override suspend fun insertAlert(entityAlert: EntityAlert) {
//            alert.add(entityAlert)
//        }
//
//        override suspend fun deleteAlert(entityAlert: EntityAlert) {
//            alert.remove(entityAlert)
//        }
//
//        override fun getAlertById(id: String): EntityAlert {
//            return alert[id.toInt()]
//        }
//    }