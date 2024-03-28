package com.example.weatherapppoject.favorite.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.utils.DBState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel (private val weatherRepository: WeatherRepositoryImpl) : ViewModel() {
    private val _favData = MutableStateFlow<DBState>(DBState.Loading())
    val currentWeather: StateFlow<DBState> = _favData
    fun addToFavorites(fav: WeatherResponse, long: Double, lat: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.insertfavIntoDB(fav, long, lat)
            Log.i("=======", "addToFavorites: done")

        }
    }

    fun removeFromFavorites(weatherData: WeatherResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteFromFav(weatherData)
        }
    }


    fun showFavItems() {
        viewModelScope.launch(Dispatchers.IO) {

            weatherRepository.getFavoriteData()
                .catch { exception ->
                    _favData.value = DBState.Failure(exception)
                }
                .collect { data ->
                    _favData.value = DBState.Suceess(data)
                }
        }
    }


    fun showWeatherDetails(longitude: Double, latitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {

            weatherRepository.getFavCityInfo(longitude,latitude)
                .catch { exception ->
                    _favData.value = DBState.Failure(exception)
                }
                .collect { data ->
                    _favData.value = DBState.OneCitySucess(data)
                }
        }
    }

    suspend fun getAddressFromCoordinates(latitude: Double, longitude: Double, context:Context): String = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context)

        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses?.isNotEmpty() == true) {
                val address: Address? = addresses?.get(0) ?: null
                val addressStringBuilder = StringBuilder()

                if (address != null) {
                    for (i in 0..address.maxAddressLineIndex) {
                        addressStringBuilder.append(address.getAddressLine(i))
                        if (i < address.maxAddressLineIndex) {
                            addressStringBuilder.append(", ")
                        }
                    }
                }

                addressStringBuilder.toString()
            } else {
                "No address found"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

}




