package com.example.weatherapppoject.alert.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.onecall.model.Alert
import com.example.weatherapppoject.onecall.model.OneApiCall
import com.example.weatherapppoject.repository.WeatherRepositoryInter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertViewModel (private val repo: WeatherRepositoryInter): ViewModel() {
    private var _alertData: MutableLiveData<List<AlertData>> = MutableLiveData<List<AlertData>>()
    val alertData: LiveData<List<AlertData>> = _alertData
    init {
        getAlertData()
    }

    fun getAlertData() {
        viewModelScope.launch (Dispatchers.IO){
            repo.getAlertedData()?.collect{
                _alertData.postValue(it)
            }
        }
    }
    fun insertIntoAlert(alertData: AlertData){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertAlerts(alertData)
            getAlertData()
        }

    }
    fun deleteFromAlert(alertData: AlertData){
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteFromAlerts(alertData)
            getAlertData()
        }
    }


}
