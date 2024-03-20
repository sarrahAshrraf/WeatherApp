package com.example.weatherapppoject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.repository.WeatherRepositoryInter

class HomeFragmentViewModelFactory (
    val weathRepo: WeatherRepositoryImpl
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)){
            HomeFragmentViewModel(weathRepo) as T}
        else{
            throw IllegalArgumentException("viewModel class notfound")
        }
    }

}