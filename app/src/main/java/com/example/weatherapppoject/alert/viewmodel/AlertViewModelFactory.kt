package com.example.weatherapppoject.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapppoject.repository.WeatherRepositoryInter

class AlertViewModelFactory(private val irepo: WeatherRepositoryInter) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            AlertViewModel(irepo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}