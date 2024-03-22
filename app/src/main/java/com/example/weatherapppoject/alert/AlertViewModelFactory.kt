package com.example.weatherapppoject.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapppoject.repository.WeatherRepositoryImpl

class AlertViewModelFactory ( val weathRepo: WeatherRepositoryImpl
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(weathRepo) as T}
        else{
            throw IllegalArgumentException("viewModel class notfound")
        }
    }

}