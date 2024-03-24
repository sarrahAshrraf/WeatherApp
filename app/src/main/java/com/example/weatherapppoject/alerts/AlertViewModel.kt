package com.example.weatherapppoject.alerts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapppoject.onecall.model.OneApiCall
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.utils.ALertDBState
import com.example.weatherapppoject.utils.OneCallState
import com.example.weatherapppoject.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

//class AlertViewModel(private val weathRepo: WeatherRepositoryImpl): ViewModel() {
//    //alert
//    private val _alerts = MutableStateFlow<ALertDBState>(ALertDBState.Loading())
//    val AlertData: StateFlow<ALertDBState> = _alerts
//
//    private val _alertData = MutableStateFlow<OneCallState>(OneCallState.Loading())
//    val alertsData: StateFlow<OneCallState> = _alertData
//
////        //==========>network call
////    fun getAlertsInfo(latitude: Double, longitude: Double) {
////        viewModelScope.launch(Dispatchers.IO) {
////            val units = "metric"
////            val apiKey = Utils.APIKEY
////            val lang = "en"
////            weathRepo.getAlertData(46.8182, 8.2275, units, apiKey, lang).collect {
//////            weatherRepository.getAlertData(31.2683793, 30.006182, units, apiKey, lang).collect {
////                _alertData.value = OneCallState.Suceess(it)
////            }
////        }
////    }
////
////    //============?data base
////    fun addToAlerts(alerts: OneApiCall, long: Double, lat: Double) {
////        viewModelScope.launch(Dispatchers.IO) {
////            weathRepo.insertAlertIntoDB(alerts, long, lat)
////            Log.i("=======", "addToFavorites: done")
//////            withContext(Dispatchers.Main){
//////                Toast.makeText(context,"item added",Toast.LENGTH_SHORT).show()
//////            }
////        }
////    }
////
////
////
////
////    fun removeFromALerts(weatherData: OneApiCall) {
////        viewModelScope.launch(Dispatchers.IO) {
////            weathRepo.deleteFromAlerts(weatherData)
////        }
////    }
////
////
////    fun showAlertsItems() {
////        viewModelScope.launch(Dispatchers.IO) {
////
////            weathRepo.getAlertedData()
////                .catch { exception ->
////                    _alerts.value = ALertDBState.Failure(exception)
////                }
////                ///edits here==> products.collect
////                .collect { data ->
////                    _alerts.value = ALertDBState.Suceess(data)
////                }
////        }
////    }
////
////
////
////
////
////
////
////
////
//
//
//
//}