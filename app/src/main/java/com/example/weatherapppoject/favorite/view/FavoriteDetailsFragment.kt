package com.example.weatherapppoject.favorite.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FragmentFavoriteDetailsBinding
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.ApiState
import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.view.FiveDaysAdapter
import com.example.weatherapppoject.view.TodayDataAdapter
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteDetailsBinding

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSourceInte: LocalDataSourceInte
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var viewModelFactory: FavoriteViewModelFactory
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    private lateinit var adapter: FiveDaysAdapter
    private lateinit var todayadapter: TodayDataAdapter
    private lateinit var viewModel: HomeFragmentViewModel
    lateinit var HomeviewModelFactory: HomeFragmentViewModelFactory
    private lateinit var fuoadapter : FavoritesAdapter
    private var language : String ="default"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository = WeatherRepositoryImpl.getInstance(remoteDataSource, localDataSourceInte)

        viewModelFactory = FavoriteViewModelFactory(repository)
        favoriteViewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)

        HomeviewModelFactory = HomeFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, HomeviewModelFactory).get(HomeFragmentViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language = sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "default")

        val longLatArray = arguments?.getDoubleArray("longlat")
        if (longLatArray != null && longLatArray.size == 2) {
            val longitude = longLatArray[0]
            val latitude = longLatArray[1]


            viewModel.getFiveDaysWeather(latitude,longitude,language)

            lifecycleScope.launch(Dispatchers.Main) {

                viewModel.fiveDaysWeather.collectLatest { weatherResponse ->
                    when (weatherResponse) {
                        is ApiState.Suceess -> {
                            binding.todayDetailsRecView.visibility = View.VISIBLE
                            binding.todayDetailsRecView.visibility = View.VISIBLE
                            val forecastList = weatherResponse.data.list
                            val forecastItems = forecastList
                                .take(8)// Display only the first 5 forecast items
                            adapter = FiveDaysAdapter(forecastItems,language)
                            binding.todayDetailsRecView.adapter = adapter
                        }

                        is ApiState.Loading -> {
                            lifecycleScope.launch(Dispatchers.Main) {
                                favoriteViewModel.showWeatherDetails(longitude, latitude)

                                favoriteViewModel.currentWeather.collect { state ->
                                    when (state) {
                                        is DBState.Loading -> {
                                            Log.i("====loading details name", "fav details fragment: ")

                                        }

                                        is DBState.OneCitySucess -> {
                                            binding.apply {
                                                city.text = state.cityData.city.name
                                                tvTemp.text = state.cityData.list[0].main.temp.toString()
                                                tvHumidity.text = state.cityData.list[0].main.humidity.toString()
                                                tvPressure.text = state.cityData.list[0].main.pressure.toString()
                                                cloudPercent.text = state.cityData.list[0].clouds.all.toString()
                                                tvStatus.text = state.cityData.list[0].weather[0].description





                                                Log.i(
                                                    "====city name",
                                                    "fav details fragment: " + state.cityData.cod
                                                )


                                            }
                                        }

                                        else -> {

                                            binding.cloudPercent.visibility = View.GONE

                                        }
                                    }


                                }
                            }
                        }

                        else -> {//fetch from database


                        }
                    }
                }


            }

            lifecycleScope.launch(Dispatchers.Main) {

                viewModel.fiveDaysWeather.collectLatest { weatherResponse ->
                    when (weatherResponse) {
                        is ApiState.Suceess -> {
                            binding.FivedaysRec.visibility = View.VISIBLE
                            binding.todayDetailsRecView.visibility = View.VISIBLE

                            val date = Utils.getDatefortvDate(weatherResponse.data.list[0].dt_txt)

                            val filteredList = weatherResponse.data.list.filter { forecastData ->
                                val time = forecastData.dt_txt.split(" ")[1]
                                val hour = time.split(":")[0].toInt()
                                hour == 12
                            }
                            todayadapter = TodayDataAdapter(filteredList,language)
                            binding.FivedaysRec.adapter = todayadapter
                        }

                        is ApiState.Loading -> {
                            binding.tvclouds.visibility = View.GONE
                            lifecycleScope.launch(Dispatchers.Main) {
                                favoriteViewModel.showWeatherDetails(longitude, latitude)

                                favoriteViewModel.currentWeather.collect { state ->
                                    when (state) {
                                        is DBState.Loading -> {
                                            Log.i("====loading details name", "fav details fragment: ")

                                        }

                                        is DBState.OneCitySucess -> {
                                            binding.apply {
                                                city.text = state.cityData.city.name
                                                tvTemp.text = state.cityData.list[0].main.temp.toString()
                                                tvHumidity.text = state.cityData.list[0].main.humidity.toString()
                                                tvPressure.text = state.cityData.list[0].main.pressure.toString()
                                                cloudPercent.text = state.cityData.list[0].clouds.all.toString()
                                                tvStatus.text = state.cityData.list[0].weather[0].description





                                                Log.i(
                                                    "====city name",
                                                    "fav details fragment: " + state.cityData.cod
                                                )


                                            }
                                        }

                                        else -> {

                                            binding.cloudPercent.visibility = View.GONE

                                        }
                                    }


                                }
                            }
                        }

                        else -> {


                        }
                    }
                }

        }





    }

    }
    }
