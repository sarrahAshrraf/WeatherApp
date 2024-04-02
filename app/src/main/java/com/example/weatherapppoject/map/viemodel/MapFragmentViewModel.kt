package com.example.weatherapppoject.map.viemodel

import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapppoject.R
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.repository.WeatherRepositoryInter
import com.example.weatherapppoject.utils.ForeCastApiState
import com.example.weatherapppoject.utils.Utils
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.debounce

class MapFragmentViewModel(private val weatherRepository: WeatherRepositoryInter,
                           private val geocoder: Geocoder,
                           private val context: Context
    ) : ViewModel() {

    private val _WeatherInfo = MutableStateFlow<ForeCastApiState>(ForeCastApiState.Loading())
    val weatherData: StateFlow<ForeCastApiState> = _WeatherInfo

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableSharedFlow<String>()
    val searchResults: SharedFlow<String> = _searchResults.asSharedFlow()
    private val _cameraMoveEvent = MutableSharedFlow<LatLng>()
    val cameraMoveEvent: SharedFlow<LatLng> = _cameraMoveEvent.asSharedFlow()

    init {
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery.debounce(3000)
                .filter { it.length >= 3 }
                .collect { query ->
                    searchLocation(query)
                }
        }
    }

    fun moveCameraToLocation(latLng: LatLng) {
        viewModelScope.launch {
            _cameraMoveEvent.emit(latLng)
        }
    }

    fun addMarkerToMap(latLng: LatLng, locationName: String) {
        viewModelScope.launch {
            _cameraMoveEvent.emit(latLng)
        }
    }
    private fun searchLocation(locationName: String) {
        val addresses = geocoder.getFromLocationName(locationName, 1)
        if (addresses!!.isNotEmpty()) {
            val address = addresses[0]
            val latLng = LatLng(address.latitude, address.longitude)
            _searchResults.tryEmit(latLng.toString())
//            Toast.makeText(context, latLng.toString(), Toast.LENGTH_SHORT).show()
                        moveCameraToLocation(latLng)
            addMarkerToMap(latLng, locationName)
        } else {
            Toast.makeText(context, context.getString(R.string.notfoundlocation), Toast.LENGTH_SHORT).show()
        }
    }
    fun setSearchQuery(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }

    fun getForecastData(latitude: Double, longitude: Double, lang: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiKey = Utils.APIKEY
            weatherRepository.getFiveDaysWeather(latitude, longitude, units, apiKey, lang).collect {
                _WeatherInfo.value = ForeCastApiState.Suceess(it)
            }
        }
    }

}
