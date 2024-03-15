package com.example.weatherapppoject.view


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.health.connect.datatypes.ExerciseRoute.Location
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.http.HttpException
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
import androidx.annotation.RequiresExtension
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapppoject.R
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.databinding.FragmentHomeBinding
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.network.RetrofitInstance
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.viewmodel.HomeFragmentViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale




class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: FiveDaysAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    lateinit var remoteDataSource: RemoteDataSource
    lateinit var repository : WeatherRepositoryImpl
    lateinit var viewModelFactory: HomeFragmentViewModelFactory
    private val locationID = 5


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
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location: android.location.Location? = locationResult.lastLocation
                if (location != null) {
                    var long = location.longitude
                    var lat = location.latitude
                    viewModel.getCurrentWeather(location.latitude, location.longitude)
                }  else {
                    Toast.makeText(requireContext(), "Location is not available", Toast.LENGTH_SHORT).show()
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

    private fun enableLocation() {
        Toast.makeText(requireContext(),"Turn On location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)

    }


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        remoteDataSource = RemoteDataSourceImp()
        repository = WeatherRepositoryImpl.getInstance(remoteDataSource)
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

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.todayDetailsRecView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        viewModel.currentWeather.observe(viewLifecycleOwner) { weatherList ->
            binding.tvTemp.text = "${weatherList.main?.temp}°C"
            binding.tvPressure.text = Utils.convertToArabicNumber(weatherList.main?.pressure.toString())

           //                Utils.convertToArabicNumber( response.body()!!.main?.temp.toString())
            binding.cloudPercent.text= "${weatherList.clouds!!.all.toString()}%"
            binding.windPercent.text = weatherList.wind!!.speed.toString()
            binding.tvDayFormat.text = weatherList.dtTxt
            binding.humidityPercent.text = weatherList.main?.humidity.toString()
            binding.pressurePercent.text = weatherList.main?.pressure.toString()
            binding.tvStatus.text = weatherList.weather[0].description
            binding.tvDayFormat.text = weatherList.dtTxt?.let { Utils.getDateAndTime(it) }//////NULL


             fun fetchLocation(){
                val task: Task<android.location.Location> = fusedLocationProviderClient.lastLocation
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                task.addOnSuccessListener {
//            val geocoder = Geocoder(requireContext(),Locale.getDefault())
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        geocoder.getFromLocation(it.latitude,it.longitude,1,object: Geocoder.GeocodeListener{
                            override fun onGeocode(address: MutableList<Address>) {
//                            weatherList.weather[0].main.
                            }
                        } )

                    }

                }


            }



            val iconId = weatherList!!.weather[0].icon
            val imgURL = "https://openweathermap.org/img/w/$iconId.png"

            when (iconId) {
                "01d" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.ddsunny)
                        .into(binding.weatherImgView)
                }
                "01n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.sunny)
                        .into(binding.weatherImgView)
                }
                "02d" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.partlycloudy)
                        .into(binding.weatherImgView)
                }
                "02n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.cloudynight)
                        .into(binding.weatherImgView)
                }
                "03d", "03n", "04d", "04n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.cloudy)
                        .into(binding.weatherImgView)
                }
                "09d", "09n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.ddrainy)
                        .into(binding.weatherImgView)
                }
                "10d", "10n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.rainy)
                        .into(binding.weatherImgView)
                }
                "11d", "11n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.thunderstorm)
                        .into(binding.weatherImgView)
                }
                "13d", "13n" -> {
                    Glide.with(requireContext())
                        .load(R.drawable.snow)
                        .into(binding.weatherImgView)
                }
                "50d", "50n" -> {
                    Glide.with(requireContext())
//                        .load(R.drawable.mist)
//                        .into(binding.weatherImgView)
                }
                else -> {
                    Glide.with(requireContext())
                        .load(imgURL)
                        .into(binding.weatherImgView)
                }
            }




        }

        viewModel.fiveDaysWeather.observe(viewLifecycleOwner) { weatherResponse ->
            // Update the UI with the five-day forecast data
            val forecastList = weatherResponse.list
            val forecastItems = forecastList
                .take(5)// Display only the first 5 forecast items
            adapter = FiveDaysAdapter(forecastItems)
            binding.todayDetailsRecView.adapter = adapter
        }

//        viewModel.getCurrentWeather()
        viewModel.getFiveDaysWeather()
    }
}







//
//class HomeFragment : Fragment() {
//    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
//    lateinit var binding: FragmentHomeBinding
//    private lateinit var viewModel: HomeFragmentViewModel
//
//    lateinit var recyclerView: RecyclerView
////    lateinit var adapter: FiveDaysAdapter
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
//    val remoteDataSource = YourRemoteDataSourceImplementation() // Implement the RemoteDataSource interface
//    val repository = WeatherRepositoryImpl(remoteDataSource)
//    val viewModelFactory = HomeFragmentViewModelFactory(repository)
//    viewModel = ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
//

//    }
//


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val locale = Locale(sharedPreferencesManager.getString(SharedKey.LANGUAGE.name, "default"))
//        Locale.setDefault(locale)
//        val resources = requireContext().resources
//        val config = Configuration(resources.configuration)
//        config.setLocale(locale)
//        val context= ContextUtils.wrapContext(requireContext(), locale)
//
////        binding.swipeRefreshLayout.setOnRefreshListener {
////            findNavController(view).navigate(R.id.home)
////        }
//
//
//        getCurrentWeather()
//
//        getForeCast()
//        ///recView
////        adapter = FiveDaysAdapter()
//        binding.todayDetailsRecView.apply {
//            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
////            adapter = this.adapter
//        }
//
//
//
//        binding.city.setText(context.getString(R.string.city))
//        binding.tvWind.setText(context.getString(R.string.winds))
//        binding.textDays.setText(context.getString(R.string.language))
//        binding.tvPressure.setText(context.getString(R.string.pressure))
//        binding.textDays.setText(context.getString(R.string.next5days))
//        binding.tvTemp.setText(context.getString(R.string.weathertemp))
//        binding.tvHumidity.setText(context.getString(R.string.humidity))
//        binding.tvclouds.setText(context.getString(R.string.clouds))
////        binding.tvDayFormat.setText(context.getString(R.string.dayformat))
//        binding.tvStatus.setText(context.getString(R.string.weatherstatus))
//    }
//
//
//@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//private fun getForeCast() {
//    GlobalScope.launch(Dispatchers.IO) {
//        val response = try {
//            RetrofitInstance.wetherAPi.getForeCast("japan", "metric", Utils.APIKEY,"ar")
//        } catch (e: IOException) {
////            Toast.makeText(requireContext(), "error" + e, Toast.LENGTH_SHORT).show()
//            Log.i("============", "Error" + e)
//            return@launch
//        } catch (e: HttpException) {
////            Toast.makeText(requireContext(), "" + e, Toast.LENGTH_SHORT).show()
//            Log.i("============", "Error" + e)
//
//            return@launch
//
//
//        }
//
//        if (response.isSuccessful && response.body() != null) {
//
//            withContext(Dispatchers.Main) {
//                val data = response.body()!!
//                var forecastArray = arrayListOf<ForeCastData>()
//                forecastArray = data.list as ArrayList<ForeCastData>
//                val adapter = FiveDaysAdapter(forecastArray)
//                binding.todayDetailsRecView.adapter = adapter
//            }
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//private fun getCurrentWeather(){
//    GlobalScope.launch (Dispatchers.IO){
//        val response = try {
//            RetrofitInstance.wetherAPi.getCureentWeather("egypt","metric", Utils.APIKEY)
//        } catch (e: IOException){
////        Toast.makeText(requireContext(),"error"+e,Toast.LENGTH_SHORT).show()
//            Log.i("============", "Error"+e)
//        return@launch
//    } catch (e :HttpException){
////            Toast.makeText(requireContext(),""+e,Toast.LENGTH_SHORT).show()
//            Log.i("============", "Error"+e)
//
//            return@launch
//
//        }
//
//    if(response.isSuccessful && response.body()!=null){
//        Log.i("===========", "city: "+response.body()!!.clouds.toString())
//        Log.i("===========", "city: "+response.body()!!.main?.temp)
//
//        withContext(Dispatchers.Main){
//           val data = response.body()
////            binding.tvDayFormat.text= SimpleDateFormat(
////                "hh:mm a",
////                Locale.ENGLISH
////            ).format(data.sys.)
//           binding.apply {
//               //TODO check if the setting is ar and convert the numbers
//               //TODO add the lang property in the api call like the forecast in APIservice
//               tvTemp.text =
//                   Utils.convertToArabicNumber( response.body()!!.main?.temp.toString())
////                   response.body()!!.main?.temp.
////               toString()
//               cloudPercent.text= response.body()!!.clouds!!.all.toString()
//               windPercent.text = response.body()!!.wind!!.speed.toString()
//               tvDayFormat.text = response.body()!!.dtTxt
//               humidityPercent.text = response.body()!!.main?.humidity.toString()
//               pressurePercent.text = response.body()!!.main?.pressure.toString()
//               tvStatus.text = response.body()!!.weather[0].description
//               tvDayFormat.text = response.body()!!.dtTxt?.let { Utils.getDateAndTime(it) }//////NULL
//
//
//
//
//           }
///*
// icons : day d + night n
// 01 - sunny
// 02 - sun +cloud
// 03 - one cloud
// 04 - clouds
// 09 - rains
//*/
//
//            val iconId = data!!.weather[0].icon
//            val imgURL = "https://openweathermap.org/img/w/$iconId.png"
//
//            Log.i("===========icin", "getCurrentWeather: "+data.weather[0].icon)
//
//            when (iconId) {
//                "01d" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.ddsunny)
//                        .into(binding.weatherImgView)
//                }
//                "01n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.sunny)
//                        .into(binding.weatherImgView)
//                }
//                "02d" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.partlycloudy)
//                        .into(binding.weatherImgView)
//                }
//                "02n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.cloudynight)
//                        .into(binding.weatherImgView)
//                }
//                "03d", "03n", "04d", "04n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.cloudy)
//                        .into(binding.weatherImgView)
//                }
//                "09d", "09n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.ddrainy)
//                        .into(binding.weatherImgView)
//                }
//                "10d", "10n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.rainy)
//                        .into(binding.weatherImgView)
//                }
//                "11d", "11n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.thunderstorm)
//                        .into(binding.weatherImgView)
//                }
//                "13d", "13n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.snow)
//                        .into(binding.weatherImgView)
//                }
//                "50d", "50n" -> {
//                    Glide.with(requireContext())
////                        .load(R.drawable.mist)
////                        .into(binding.weatherImgView)
//                }
//                else -> {
//                    Glide.with(requireContext())
//                        .load(imgURL)
//                        .into(binding.weatherImgView)
//                }
//            }
////
////            if (iconId =="04n"){
////                val imgURL = "https://openweathermap.org/img/w/$iconId.png"
////                Glide.with(requireContext())
////                    .load(R.drawable.cloudy)
////                    .into(binding.weatherImgView)
////            }
//
//        }
//    }
//    }
//}
//}
