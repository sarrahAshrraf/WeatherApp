package com.example.weatherapppoject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.WeatherAppProject.Weather
import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.repository.WeatherRepositoryInter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragmentViewModel(private val weatherRepository: WeatherRepositoryImpl) : ViewModel() {
    private val _currentWeather = MutableLiveData<WeatherList>()
    val currentWeather: LiveData<WeatherList> = _currentWeather

    private val _fiveDaysWeather = MutableLiveData<WeatherResponse>()
    val fiveDaysWeather: LiveData<WeatherResponse> = _fiveDaysWeather

    fun getCurrentWeather() {
        viewModelScope.launch {
            try {
                val weatherList = weatherRepository.getCurrentWeather()
                _currentWeather.value = weatherList
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }

    fun getFiveDaysWeather() {
        viewModelScope.launch {
            try {
                val weatherResponse = weatherRepository.getFiveDaysWeather()
                _fiveDaysWeather.value = weatherResponse
            } catch (e: Exception) {
                Log.i("+======", "getFiveDaysWeather: Eroor" +e)
            }
        }
    }
}



//class HomeFragmentViewModel(private val weatherRepository: WeatherRepositoryImpl) : ViewModel()  {
//    private val weatherData = MutableLiveData<List<Weather>>() //observable -> emit
//    val data: LiveData<List<Weather>> = weatherData //observable
//
//    //retrofit
//    fun getCurrentWeatherData() {
//        viewModelScope.launch (Dispatchers.IO){
//            val weatherList = weatherRepository.getCurrentWeather()
//            weatherData.postValue(weatherList)
//
//        }
//    }
//}