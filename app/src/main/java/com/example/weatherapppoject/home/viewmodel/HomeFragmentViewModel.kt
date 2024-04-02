package com.example.weatherapppoject.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.repository.WeatherRepositoryInter
import com.example.weatherapppoject.utils.ForeCastApiState
import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class HomeFragmentViewModel(private val weatherRepository: WeatherRepositoryInter) : ViewModel() {

    private val _fiveDaysWeather = MutableStateFlow<ForeCastApiState>(ForeCastApiState.Loading())
    val weatherData: StateFlow<ForeCastApiState> = _fiveDaysWeather

    private val _favData = MutableStateFlow<DBState>(DBState.Loading())
    val favDataHome: StateFlow<DBState> = _favData


    fun addWeatherDataIntoDB(fav: WeatherResponse, long: Double, lat: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.insertHomeData(fav, long, lat)
        }
    }


    fun showWeatherDataFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getFavCityInfoHome()
                .catch { exception ->
                    _favData.value = DBState.Failure(exception)
                }
                .collect { data ->
                    if (checkNotNull(false)){
                        _favData.value = DBState.Failure(Throwable())
                }else {
                        _favData.value = DBState.OneCitySucess(data)

                    }
                }
        }
    }

    fun removeFromDataBase(weatherResponse: WeatherResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteFromHome(weatherResponse)
        }
    }


    fun getFiveDaysWeather(latitude: Double, longitude: Double, lang: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiKey = Utils.APIKEY
            weatherRepository.getFiveDaysWeather(latitude, longitude, units, apiKey, lang).collect {
                _fiveDaysWeather.value = ForeCastApiState.Suceess(it)
            }
        }
    }
}

