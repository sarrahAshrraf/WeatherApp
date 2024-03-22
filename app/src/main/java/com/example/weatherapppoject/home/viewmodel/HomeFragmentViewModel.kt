package com.example.weatherapppoject.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.repository.WeatherRepositoryInter
import com.example.weatherapppoject.utils.ALertDBState
import com.example.weatherapppoject.utils.ApiState
import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.utils.OneCallState
import com.example.weatherapppoject.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeFragmentViewModel(private val weatherRepository: WeatherRepositoryImpl) : ViewModel() {
//    private val _currentWeather = MutableLiveData<WeatherList>()
//    val currentWeather: LiveData<WeatherList> = _currentWeather

    private val _currentWeather = MutableStateFlow<ApiState>(ApiState.Loading())
    val currentWeather: StateFlow<ApiState> = _currentWeather

    private val _alertData = MutableStateFlow<OneCallState>(OneCallState.Loading())
    val alertsData: StateFlow<OneCallState> = _alertData

    private val _fiveDaysWeather = MutableStateFlow<ApiState>(ApiState.Loading())
    val fiveDaysWeather: StateFlow<ApiState> = _fiveDaysWeather



////alert
//    private val _alerts = MutableStateFlow<ALertDBState>(ALertDBState.Loading())
//    val AlertData: StateFlow<ALertDBState> = _alerts




//
//    private val _alertData = MutableLiveData<OneApiCall>()
//    val alertsData: LiveData<OneApiCall> = _alertData

//    fun getalertsInfo(latitude: Double, longitude: Double) {
//        viewModelScope.launch {
//          try{  val units = "metric"
//            val apiKey = Utils.APIKEY
//
//            val weatherList = weatherRepository.getAlertData(latitude, longitude, units, apiKey,"en")
//              _alertData.value = weatherList}
//
//          catch (e: Exception) {
//              Log.i("+======", "getFiveDaysWeather: Eroor" +e)
//          }
//        }
//    }

    //TODo
    fun getCurrentWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val units = "metric"
            val apiKey = Utils.APIKEY
            weatherRepository.getFiveDaysWeather(latitude, longitude, units, apiKey, "en").collect {
                _fiveDaysWeather.value = ApiState.Suceess(it)
            }
        }
    }


    fun getAlertsInfo(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val units = "metric"
            val apiKey = Utils.APIKEY
            val lang = "en"
            weatherRepository.getAlertData(46.8182, 8.2275, units, apiKey, lang).collect {
//            weatherRepository.getAlertData(31.2683793, 30.006182, units, apiKey, lang).collect {
                _alertData.value = OneCallState.Suceess(it)
            }
        }
    }

    fun getFiveDaysWeather(latitude: Double, longitude: Double, lang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val units = "metric"
            val apiKey = Utils.APIKEY
//            val lang = "en"
            weatherRepository.getFiveDaysWeather(latitude, longitude, units, apiKey, lang).collect {
                _fiveDaysWeather.value = ApiState.Suceess(it)
            }
        }
    }

//insert alert data




//    private val _favCity = MutableStateFlow<DBState>(DBState.Loading())
//    val currentWeather: StateFlow<DBState> = _favCity

    fun addToAlerts(alerts: OneApiCall, long: Double, lat: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.insertAlertIntoDB(alerts, long, lat)
            Log.i("=======", "addToFavorites: done")
//            withContext(Dispatchers.Main){
//                Toast.makeText(context,"item added",Toast.LENGTH_SHORT).show()
//            }
        }
    }

































//    fun getFiveDaysWeather(latitude: Double , longitude: Double) {
//        viewModelScope.launch {
//            try {
//                val units = "metric"
//                val apiKey = Utils.APIKEY
//                val lang = "ar"
//                val weatherResponse = weatherRepository.getFiveDaysWeather(latitude,longitude, units, apiKey,lang)
//                _fiveDaysWeather.value = weatherResponse
//            } catch (e: Exception) {
//                Log.i("+======", "getFiveDaysWeather: Eroor" +e)
//            }
//        }
//    }


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

}