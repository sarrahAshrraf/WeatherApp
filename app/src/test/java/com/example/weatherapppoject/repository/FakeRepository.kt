package com.example.weatherapppoject.repository

import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.forecastmodel.City
import com.example.weatherapppoject.forecastmodel.Clouds
import com.example.weatherapppoject.forecastmodel.Coord
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.Main
import com.example.weatherapppoject.forecastmodel.Weather
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.forecastmodel.Wind
import com.example.weatherapppoject.onecall.model.OneApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository :WeatherRepositoryInter{

    var home : MutableList<WeatherResponse> = mutableListOf()
    var favorite : MutableList<WeatherResponse> = mutableListOf()
    var alert : MutableList<AlertData> = mutableListOf()

    val latitude = 78.9
    val longitude = 123.456
    val lang = "en"
    val units = "metric"
    val city = City(Coord(12.345, 67.890), "Country", 123, "City", 100000, 123456, 789012, 3600)
    val forecastList = mutableListOf(
        ForeCastData(
            Clouds(0),
            1658886000,
            "2024-03-26 12:00:00",
            Main(25.0, 15, 20, 1902, 30, 22.0, 90.0, 100.0, 10.0),
            10000,
            mutableListOf(Weather("Clear", "01d", 800, "Clear Sky")),
            Wind(10, 2.5, 3.4)
        )
    )
    val fakeWeatherData = WeatherResponse(
        9,
        city,
        123.456,
        78.90,
        5,
        0,
        0,
        "200",
        forecastList,
        0
    )


    override suspend fun getFiveDaysWeather(
        latitudee: Double,
        longitudee: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<WeatherResponse> {
        return  flowOf(WeatherResponse(9,city, longitude,latitude,5,0,0,"200",forecastList,0))
    }

    override fun getFavoriteData(): Flow<List<WeatherResponse>> = flowOf(favorite)

    override suspend fun insertfavIntoDB(
        fav: WeatherResponse,
        longitude: Double,
        latitude: Double
    ) {
        favorite.add(fav)
    }

    override suspend fun deleteFromFav(weatherData: WeatherResponse) {
        favorite.remove(weatherData)
    }

    override suspend fun deleteFromHome(weatherData: WeatherResponse) {
        home.remove(weatherData)
    }

    override fun getAlertedData(): Flow<List<AlertData>> = flowOf(alert)

    override suspend fun insertAlertIntoDB(alerts: AlertData, longitude: Double, latitude: Double) {
        alert.add(alerts)
    }

    override suspend fun insertAlerts(alert: AlertData) {
        this.alert.add(alert)
    }

    override suspend fun deleteFromAlerts(alertWeatherData: AlertData) {
        alert.remove(alertWeatherData)
    }


    override suspend fun getAlertData(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): Flow<OneApiCall> {
        TODO("Not yet implemented")
    }


    override fun getFavCityInfo(longitude: Double, latitude: Double): Flow<WeatherResponse> {
      return flowOf(favorite.first())
    }

    override suspend fun insertHomeData(
        weatherData: WeatherResponse,
        longitude: Double,
        latitude: Double
    ) {
        home.add(weatherData)
    }

    override fun getFavCityInfoHome(): Flow<WeatherResponse> {
        return flowOf(home.first())
    }






//    override fun getFavCityInfoHome(): Flow<WeatherResponse> = flow {
//        if (home.isNotEmpty()) {
//            emit(home.first())
//        } else {
//            throw NoSuchElementException("No favorite cities found.")
//        }
//    }

    override suspend fun deleteHome() {
        home.clear()
    }


}