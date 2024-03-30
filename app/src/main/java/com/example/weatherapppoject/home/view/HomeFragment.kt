package com.example.weatherapppoject.home.view


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapppoject.R
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FragmentHomeBinding
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.ForeCastApiState
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModelFactory

import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.utils.NetworkStateReceiver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale



class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var todayAdapter: TodayAdapter
    private lateinit var weekAdapter: WeekAdapter
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    lateinit var remoteDataSource: RemoteDataSource
    lateinit var localDataSourceInte: LocalDataSourceInte
    lateinit var repository: WeatherRepositoryImpl
    lateinit var viewModelFactory: HomeFragmentViewModelFactory
    private val locationID = 5
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favviewModelFactory: FavoriteViewModelFactory
    private var mainLongitude:Double =0.0
    private var mainLatitude: Double =0.0
    private var language : String = "default"
    private var units : String = "metric" //"metric" celisuc
    private var tempUnit : String="metric"
//    private var countryName : String =""
//    private var networkStateReceiver: NetworkStateReceiver? = null
    //default -> kalvin ,,,, imperial: F

    override fun onStart() {
        super.onStart()
        if (checkPermission()) {
            if (context?.let { isLocationEnabled(it) } == true) {
                getFreshLocation()
                Toast.makeText(requireContext(), "location is enabled", Toast.LENGTH_SHORT).show()
            } else {
                enableLocation()
            }
        } else {
            requestPermission()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository = WeatherRepositoryImpl.getInstance(remoteDataSource,localDataSourceInte)
        favviewModelFactory = FavoriteViewModelFactory(repository)
        favoriteViewModel = ViewModelProvider(this, favviewModelFactory).get(FavoriteViewModel::class.java)
        viewModelFactory = HomeFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "ResourceType", "ShowToast")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferencesManager.setMap(SharedKey.MAP.name, "home")
        binding.todayDetailsRecView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.FivedaysRec.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if(checkForInternet(requireContext())){
            getWeatherData()
            getFiveDaysData()
            getTodayData()

        }
        else {
            getWeathDataFromDB()
        }
//
//        networkStateReceiver = NetworkStateReceiver { isConnected ->
//            if (isConnected) {
//                getWeatherData()
//                getFiveDaysData()
//                getTodayData()
//
//            }
//            else { //db
//                getWeathDataFromDB()
//            }
//        }
//        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        requireContext()?.registerReceiver(networkStateReceiver, intentFilter)
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        requireContext()?.unregisterReceiver(networkStateReceiver)
//        networkStateReceiver = null
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun getWeatherData(){
        lifecycleScope.launch(Dispatchers.Main) {

            viewModel.weatherData.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is ForeCastApiState.Suceess -> {
                        binding.scrollView2.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
//
//                        CoroutineScope(Dispatchers.IO).launch {
//                            viewModel.removeFromDataBase()
//                            Log.i("===db scope", "onViewCreated: ")
//                        }
                        binding.tvTemp.visibility = View.VISIBLE
                        binding.weatherImgView.visibility = View.VISIBLE
                        Log.i("===succe in home", "onViewCreated: ")

                        val iconId = weatherResponse.data.list[0].weather[0].icon
                        if (iconId != null) {
                            Utils.getWeatherIcon(iconId, binding.weatherImgView)
                            if (iconId == "09d" || iconId == "09n" || iconId == "10d" || iconId == "10n")
                                binding.backGrou.setAnimation(R.raw.rainbackground)
                        }

                        val tempUnit = sharedPreferencesManager.getTempUnit(SharedKey.TEMP_UNIT.name, "")

                        val temperature = weatherResponse.data.list[0].main.temp
                        val windSpeed = weatherResponse.data.list[0].wind?.speed
                        val clouds = weatherResponse.data.list[0].clouds?.all.toString()
                        val dateTime = weatherResponse.data.list[0].dt_txt
                        val humidity = weatherResponse.data.list[0].main.humidity.toString()
                        val pressure = weatherResponse.data.list[0].main.pressure.toString()
                        val description = weatherResponse.data.list[0].weather[0].description

                        val isArabicLanguage = sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "") == "ar"
                        val isMetricUnits = sharedPreferencesManager.getUnitsType(SharedKey.UNITS.name, "")

//                        setLocale(if (isArabicLanguage) "ar" else "en")

                        binding.tvTemp.text = buildTemperatureText(temperature, tempUnit, isArabicLanguage)
                        binding.windPercent.text = buildWindSpeedText(windSpeed, isMetricUnits,tempUnit, isArabicLanguage) +"%"
                        binding.cloudPercent.text = buildText(clouds, isArabicLanguage)
                        binding.tvDayFormat.text = buildText(Utils.getDate(dateTime).toString(),isArabicLanguage)
                        binding.humidityPercent.text = buildText(humidity,isArabicLanguage)
                        binding.pressurePercent.text = buildText(pressure,isArabicLanguage)
                        binding.tvStatus.text = description

                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.addWeatherDataIntoDB(
                                weatherResponse.data,
                                weatherResponse.data.city.coord.lon,
                                weatherResponse.data.city.coord.lat
                            )
                            Log.i("===db scope", "onViewCreated: ")
                        }

                    }

                    is ForeCastApiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.scrollView2.visibility = View.GONE
                    }

                    else -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.scrollView2.visibility = View.GONE
                        Log.i("===failure in home", "onViewCreated: ")

                    }
                }
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun getWeathDataFromDB(){
        Log.i("=======db enter fun", "getWeathDataFromDB: ")
        CoroutineScope(Dispatchers.IO).launch {
                viewModel.showWeatherDataFromDB()}
//        viewModel.showWeatherDataFromDB()
        lifecycleScope.launch(Dispatchers.Main) {
                viewModel.favDataHome.collect { state ->
                    when (state) {
                        is DBState.Loading -> {
                                        binding.progressBar.visibility = View.VISIBLE
                                        binding.scrollView2.visibility = View.GONE
                        }

                        is DBState.OneCitySucess -> {
                            binding.progressBar.visibility = View.GONE
                            binding.scrollView2.visibility = View.VISIBLE
                            binding.apply {
//                                city.text = state.cityData.city.name
                                tvTemp.text = state.cityData.list[0].main.temp.toString()
                                humidityPercent.text = state.cityData.list[0].main.humidity.toString()
                                pressurePercent.text = state.cityData.list[0].main.pressure.toString()
                                cloudPercent.text = state.cityData.list[0].clouds.all.toString()+"%"
                                tvStatus.text = state.cityData.list[0].weather[0].description
                                binding.tvFullocation.text = state.cityData.city.name
                                city.text =" "
                                binding.tvDayFormat.text = Utils.getDate(state.cityData.list[0].dt_txt).toString()
                                binding.windPercent.text = state.cityData.list[0].wind.speed.toString()
                                val iconId = state.cityData.list[0].weather[0].icon
                                if (iconId != null) {
                                    Utils.getWeatherIcon(iconId, binding.weatherImgView)
                                    if (iconId == "09d" || iconId == "09n" || iconId == "10d" || iconId == "10n")
                                        binding.backGrou.setAnimation(R.raw.rainbackground)
                                }
                            }
                        }

                        else -> {}
                    }

                }
            }
        }

    private fun buildTemperatureText(temperature: Double, tempUnit: String, isArabicLanguage: Boolean): String {
        val temperatureText = if (isArabicLanguage) {
            Utils.convertToArabicNumber(temperature.toString())
        } else {
            temperature.toString()
        }
        val unitText = when {
            tempUnit == "metric" -> if (isArabicLanguage) "°م" else "°C"
            tempUnit == "standard" -> if (isArabicLanguage) "°ك" else "°K"
            else -> if (isArabicLanguage) "°ف" else "°F"
        }
        return "$temperatureText$unitText"
    }

    private fun buildWindSpeedText(windSpeed: Double?, windSpeedUnit: String, tempUnit: String, isArabicLanguage: Boolean): String {
        val convertedWindSpeed: Double? = when {
            windSpeedUnit == "metric" && (tempUnit == "metric" || tempUnit == "standard") -> windSpeed
            windSpeedUnit == "metric" && tempUnit == "imperial" -> windSpeed?.let { Utils.convertToMeterPerSec(it) }
            windSpeedUnit == "imperial" && (tempUnit == "metric" || tempUnit == "standard") -> windSpeed?.let { Utils.convertToMilePerHour(it) }
            windSpeedUnit == "imperial" && (tempUnit == "imperial") -> windSpeed
            else -> 0.0

        }

        val speedText = if (isArabicLanguage) {
            Utils.convertToArabicNumber(convertedWindSpeed?.toString() ?: "")
        } else {
            convertedWindSpeed?.toString() ?: ""
        }

        val unitText = when (windSpeedUnit) {
            "metric" -> if (isArabicLanguage) "م/ث" else "m/s"
            "imperial" -> if (isArabicLanguage) "ميل/س" else "m/h"
            else -> ""
        }

        return "$speedText $unitText"
    }

    private fun buildText(txt: String, isArabicLanguage: Boolean): String {
        val tvText = if (isArabicLanguage) {
            Utils.convertToArabicNumber(txt)
        } else {
            txt
        }
        return tvText
    }

    fun getFiveDaysData() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.weatherData.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is ForeCastApiState.Suceess -> {
                        binding.scrollView2.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.todayDetailsRecView.visibility = View.VISIBLE
                        binding.todayDetailsRecView.visibility = View.VISIBLE
                        val forecastList = weatherResponse.data.list
                        val forecastItems = forecastList
                            .take(8)
                        if (sharedPreferencesManager.getLanguae(
                                SharedKey.LANGUAGE.name,
                                ""
                            ) == "ar"
                        ) {
//                            setLocale("ar")
                            todayAdapter = TodayAdapter(forecastItems, language, tempUnit)
                            binding.todayDetailsRecView.adapter = todayAdapter
                        } else {
//                            setLocale("en")
                            todayAdapter = TodayAdapter(forecastItems, language, tempUnit)
                            binding.todayDetailsRecView.adapter = todayAdapter
                        }

                    }

                    is ForeCastApiState.Loading -> {
                        binding.scrollView2.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvTodayRecTxt.visibility = View.GONE
                        binding.textDays.visibility = View.GONE
                        binding.FivedaysRec.visibility = View.GONE
                        binding.todayDetailsRecView.visibility = View.GONE
                    }

                    else -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()

                    }
                }
            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayData() {
        lifecycleScope.launch(Dispatchers.Main) {

            viewModel.weatherData.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is ForeCastApiState.Suceess -> {
                        binding.scrollView2.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.FivedaysRec.visibility = View.VISIBLE
                        binding.todayDetailsRecView.visibility = View.VISIBLE
                        val date = Utils.getDate(weatherResponse.data.list[0].dt_txt)
                        val filteredList = weatherResponse.data.list.filter { forecastData ->
                            val time = forecastData.dt_txt.split(" ")[1]
                            val hour = time.split(":")[0].toInt()
                            hour == 12
                        }
                        if (sharedPreferencesManager.getLanguae(
                            SharedKey.LANGUAGE.name, "") == "ar") {
//                            setLocale("ar")
                            weekAdapter = WeekAdapter(filteredList, language, tempUnit)
                            binding.FivedaysRec.adapter = weekAdapter
                        } else {
//                            setLocale("en")
                            weekAdapter = WeekAdapter(filteredList, language, tempUnit)
                            binding.FivedaysRec.adapter = weekAdapter
                        }

                    }

                    is ForeCastApiState.Loading -> {
                        binding.scrollView2.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.FivedaysRec.visibility = View.GONE
                        binding.todayDetailsRecView.visibility = View.GONE
                    }

                    else -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()

                    }
                }
            }


        }
    }


    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }

        val locationCallback = object : LocationCallback() {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SuspiciousIndentation")
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location: android.location.Location? = locationResult.lastLocation
                language = sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "default")
                units= sharedPreferencesManager.getUnitsType(SharedKey.UNITS.name, "")//wind
                tempUnit= sharedPreferencesManager.getTempUnit(SharedKey.TEMP_UNIT.name, "")//temp
                if (location != null && sharedPreferencesManager.getlocationChoice(SharedKey.GPS.name, "") == "gps") {
                    mainLongitude = location.longitude
                    mainLatitude = location.latitude
                    viewModel.getFiveDaysWeather(mainLatitude,mainLongitude ,language,tempUnit)
                    displayAddress(mainLatitude, mainLongitude)
                    displayfullAddress(mainLatitude, mainLongitude)
                    sharedPreferencesManager.saveLocationToAlert(SharedKey.ALERT.name, mainLongitude,mainLatitude)

                }
                else {
                    if (sharedPreferencesManager.getlocationChoice( SharedKey.GPS.name,"" ) == "map"
                        && sharedPreferencesManager.getSavedMap(SharedKey.MAP.name,"")=="home") {
                        val longlat =
                            sharedPreferencesManager.getLocationToHOme(SharedKey.Home.name)
                        mainLongitude = longlat!!.first
                        mainLatitude = longlat.second
                        viewModel.getFiveDaysWeather(mainLatitude, mainLongitude,language,tempUnit)
                        displayAddress(mainLatitude, mainLongitude)
                        displayfullAddress(mainLatitude, mainLongitude)
                        sharedPreferencesManager.saveLocationToAlert(SharedKey.ALERT.name, mainLongitude,mainLatitude)
                    }
                    else {
//                        Toast.makeText(requireContext(),"No Location defined".show()
                    }
                }

                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }



        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    fun displayAddress(latitude: Double, longitude: Double) {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val city = address.countryName
//                countryName = city
                binding.city.text = city

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun displayfullAddress(latitude: Double, longitude: Double) {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        sharedPreferencesManager.savecurrentLocationToMap(SharedKey.CURMAP.name, LatLng(latitude,longitude))

        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val locality = address.adminArea
               if (locality.isNotEmpty()) {
//                   networkStateReceiver = NetworkStateReceiver { isConnected ->
                       if (checkForInternet(requireContext())) {
                           binding.tvFullocation.text = locality!!.toString()
                       } else {
                           getWeathDataFromDB() }

                   }
//                   val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//                   requireContext()?.registerReceiver(networkStateReceiver, intentFilter)
               }
            }
        }



    private fun enableLocation() {
        Toast.makeText(requireContext(), "Turn On location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)

    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            locationID
        )
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

//    private fun setLocale(languageCode: String) {
//        val resources = requireContext().resources
//        val config = Configuration(resources.configuration)
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        config.setLocale(locale)
//        resources.updateConfiguration(config, resources.displayMetrics)
//        ViewCompat.setLayoutDirection(
//            requireActivity().window.decorView,
//            if (languageCode == "ar") ViewCompat.LAYOUT_DIRECTION_RTL else ViewCompat.LAYOUT_DIRECTION_LTR
//        )
//
//    }
//        val context= ContextUtils.wrapContext(requireContext(), locale)
//        val translatecloudsText = context.getString(R.string.clouds)
//        val translatedwindText = context.getString(R.string.winds)
//        val translatedhumidityText = context.getString(R.string.humidity)
//        val translatedpressureText = context.getString(R.string.pressure)
//        val translatedtodayRecText = context.getString(R.string.today)
//        val translatedDayRecText = context.getString(R.string.next5days)
//
//        binding.tvclouds.text = translatecloudsText
//        binding.tvWind.text = translatedwindText
//        binding.tvHumidity.text = translatedhumidityText
//        binding.tvPressure.text = translatedpressureText
//        binding.tvTodayRecTxt.text = translatedtodayRecText
//        binding.textDays.text = translatedDayRecText



}
