package com.example.weatherapppoject.map.view

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.location.Geocoder
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapppoject.R
import com.example.weatherapppoject.alert.view.AlertFragment
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FragmentMapsBinding
import com.example.weatherapppoject.favorite.view.FavoriteFragment
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.home.view.HomeFragment
import com.example.weatherapppoject.map.viemodel.MapFragmentViewModel
import com.example.weatherapppoject.map.viemodel.MapViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.ForeCastApiState
import com.example.weatherapppoject.utils.NetworkStateReceiver
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.view.HomeActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MapsFragment : Fragment() {
    private lateinit var geocoder: Geocoder
    private lateinit var binding: FragmentMapsBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var sharedPrefrencesManager: SharedPrefrencesManager
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSourceInte: LocalDataSourceInte
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var viewModelFactory: FavoriteViewModelFactory
    private lateinit var mapViewModel: MapFragmentViewModel
    private lateinit var mapFactory : MapViewModelFactory
    private var lati = 0.0
    private var longgi =0.0
    private var units : String =""
    private var latt: Double =0.0
    private var longg :Double =0.0
    private var language :String =""
    private var networkStateReceiver: NetworkStateReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefrencesManager = SharedPrefrencesManager.getInstance(requireContext())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository= WeatherRepositoryImpl.getInstance(remoteDataSource,localDataSourceInte)
        viewModelFactory = FavoriteViewModelFactory(repository)
        favoriteViewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)
        mapFactory = MapViewModelFactory(repository)
        mapViewModel = ViewModelProvider(this, mapFactory).get(MapFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Places.initialize(requireContext(), Utils.MAPS_KEY)
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkStateReceiver = NetworkStateReceiver { isConnected ->
            if (isConnected) {
                binding.tvNetworkIndicator.visibility = View.GONE
                geocoder = Geocoder(requireContext())
                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                mapFragment?.getMapAsync { map ->
                    googleMap = map
                    googleMap.setOnMapClickListener { latLng ->
                        val locationName = getAddressFromLatLng(latLng)
                        addMarkerToMap(latLng, locationName)
                    }
                }

                binding.imgCurrentLocation.setOnClickListener {
                    val myLong = 30.006179
                    val myLat = 31.2683708
                    val mylatlang = LatLng(myLat, myLong)

                    if (mylatlang != null) {
                        moveCameraToLocation(mylatlang)
                    }
                }

//                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                        // Not needed for this implementation
//                    }
//
//                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                        val searchQuery = s.toString()
//
//                        mapViewModel.setSearchQuery(searchQuery)
//                        viewLifecycleOwner.lifecycleScope.launch {
//                            searchLocation(searchQuery)
//                        }
//                        }
//
//
//                    override fun afterTextChanged(s: Editable?) {
//                        // Not needed for this implementation
//                    }
//                })

//                lifecycleScope.launchWhenStarted {
//                    mapViewModel.searchResults.collect { searchResult ->
//                        Toast.makeText(requireContext(), searchResult, Toast.LENGTH_SHORT).show()
//                    }
//                }

                binding.imgSearchIcon.setOnClickListener {

                    val searchQuery = binding.etSearchMap.text.toString()
                    if (searchQuery.isNotEmpty()) {
                        lifecycleScope.launch {
                            mapViewModel.setSearchQuery(searchQuery)
                            searchLocation(searchQuery)
                        }
                    }
                }



//
//                binding.imgSearchIcon.setOnClickListener {
//                    val searchQuery = binding.etSearchMap.text.toString()
//                    if (searchQuery.isNotEmpty()) {
//                        searchLocation(searchQuery)
//                    }
//                }

                if (sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name, "") == "fav") {
                    binding.btnSelectOrAddOnMap.text = getString(R.string.addtofavorite)
                } else if (sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name, "") == "home") {
                    binding.btnSelectOrAddOnMap.text = getString(R.string.makecurrent)
                } else {
                    binding.btnSelectOrAddOnMap.text = getString(R.string.addtoALert)
                }

                binding.btnSelectOrAddOnMap.setOnClickListener {
                    if (sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name, "") == "fav") {
                        val longlat = sharedPrefrencesManager.getLocationFromMap(SharedKey.FAV.name)
                         longg = longlat!!.first
                         latt = longlat.second
                         language = sharedPrefrencesManager.getLanguae(SharedKey.LANGUAGE.name, "default")
                         units = sharedPrefrencesManager.getUnitsType(SharedKey.UNITS.name, "")
                         saveWeatherDataAsFav()
                         replaceFragments(FavoriteFragment())
                    }
                    else if (sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name, "") == "home") {
                   val hLongLat = sharedPrefrencesManager.getLocationToHOme(SharedKey.Home.name)
                   val hLong = hLongLat!!.first
                   val hLat = hLongLat.second
                   val language = sharedPrefrencesManager.getLanguae(SharedKey.LANGUAGE.name, "default")
                   sharedPrefrencesManager.saveLocationToHOme(SharedKey.Home.name, hLong, hLat)
                        replaceFragments(HomeFragment())
                     }
                   else {
                    val alertLongLat = sharedPrefrencesManager.getLocationToAlert(SharedKey.ALERT.name)
                    val aLong = alertLongLat!!.first
                    val aLat = alertLongLat.second
                        sharedPrefrencesManager.saveLocationToAlert(SharedKey.ALERT.name, aLong, aLat)
                        replaceFragments(AlertFragment())
                    }
                }

                binding.imgClearSearch.setOnClickListener {
                    binding.etSearchMap.text.clear()
                    googleMap.clear()
                }
            } else {
                binding.tvNetworkIndicator.visibility = View.VISIBLE

            }
        }
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            requireContext()?.registerReceiver(networkStateReceiver, intentFilter)


    }

    fun saveWeatherDataAsFav(){
        lifecycleScope.launch(Dispatchers.Main) {
            mapViewModel.getForecastData(latt, longg, language, units)
            mapViewModel.weatherData.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is ForeCastApiState.Suceess -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            favoriteViewModel.addToFavorites(
                                weatherResponse.data,
                                weatherResponse.data.city.coord.lon,
                                weatherResponse.data.city.coord.lat
                            )
                        }
                    }

                    is ForeCastApiState.Loading -> {
                        Log.i("======log", "loding: ")
                    }

                    else -> {
                        Log.i("========errorrr", "rrrrr: ")
                    }
                }
            }
        }

    }

    private fun searchLocation(locationName: String) {
        val addresses = geocoder.getFromLocationName(locationName, 1)
        if (addresses!!.isNotEmpty()) {
            val address = addresses[0]
            val latLng = LatLng(address.latitude, address.longitude)
            moveCameraToLocation(latLng)
            addMarkerToMap(latLng, locationName)
        } else {
            Toast.makeText(requireContext(), getString(R.string.notfoundlocation), Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCameraToLocation(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

    private fun addMarkerToMap(latLng: LatLng, title: String) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng).title(title))
        val locationFromMark = getAddressFromLatLng(latLng)
        binding.etSearchMap.setText(locationFromMark)
        if(sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name,"")=="home"){
            Log.i("=====23low", "getAddressFromLatLng: "+sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name,""))
            sharedPrefrencesManager.saveLocationToHOme(SharedKey.Home.name, latLng.longitude,latLng.latitude)
        }
        else if(sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name,"")=="fav"){
            sharedPrefrencesManager.saveLocationFromMap(SharedKey.FAV.name, latLng.longitude,latLng.latitude)
        }
        else{
            sharedPrefrencesManager.saveLocationToAlert(SharedKey.ALERT.name, latLng.longitude,latLng.latitude)

        }

    }
    private fun getAddressFromLatLng(latLng: LatLng): String {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return if (addresses!!.isNotEmpty()) {
            addresses[0]!!.getAddressLine(0)
        } else {
            "Unknown Location"
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        requireContext()?.unregisterReceiver(networkStateReceiver)
        networkStateReceiver = null
    }
    private fun replaceFragments(fragment: Fragment) {
        val transaction = (context as HomeActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
