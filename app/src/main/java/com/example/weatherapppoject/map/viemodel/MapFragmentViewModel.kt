package com.example.weatherapppoject.map.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.repository.WeatherRepositoryInter
import com.example.weatherapppoject.utils.ForeCastApiState
import com.example.weatherapppoject.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapFragmentViewModel (private val weatherRepository: WeatherRepositoryInter) : ViewModel() {

    private val _WeatherInfo = MutableStateFlow<ForeCastApiState>(ForeCastApiState.Loading())
    val weatherData: StateFlow<ForeCastApiState> = _WeatherInfo

    fun getForecastData(latitude: Double, longitude: Double, lang: String, units:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiKey = Utils.APIKEY
            weatherRepository.getFiveDaysWeather(latitude, longitude, units, apiKey, lang).collect {
                _WeatherInfo.value = ForeCastApiState.Suceess(it)
            }
        }
    }


}