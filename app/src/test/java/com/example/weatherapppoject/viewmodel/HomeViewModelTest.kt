package com.example.weatherapppoject.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapppoject.MainRule
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.forecastmodel.City
import com.example.weatherapppoject.forecastmodel.Clouds
import com.example.weatherapppoject.forecastmodel.Coord
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.Main
import com.example.weatherapppoject.forecastmodel.Weather
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.forecastmodel.Wind
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.repository.FakeRepository
import com.example.weatherapppoject.utils.DBState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val main = MainRule()

    private lateinit var homeViewModel: HomeFragmentViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun getSetup() {
        repository = FakeRepository()
        homeViewModel = HomeFragmentViewModel(repository)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertHomeData_EntityHoem() = runBlockingTest {

        val city = City(Coord(12.345, 67.890), "Country", 123, "City", 100000, 123456, 789012, 3600)
        val forecastList = mutableListOf(
            ForeCastData(
                Clouds(0),
                1658886000,
                "2024-03-26 12:00:00",
                Main(25.0, 15, 20, 1902, 30, 22.0, 90.0, 100.0, 10.0),
                10000,
                mutableListOf(Weather("Clear", "01d", 800, "Clear Sky")),
                Wind(10, 2.5, 3.4)
            )
        )
        val favorite = WeatherResponse(
            9,
            city,
            123.456,
            78.90,
            5,
            0,
            0,
            "200",
            forecastList,
            0
        )

        val expectedAlertData = listOf(favorite)

        homeViewModel.addWeatherDataIntoDB(favorite, 9.0, 77.0)

        homeViewModel.showWeatherDataFromDB()

        launch {

            homeViewModel.favDataHome.collect {
                when (it) {
                    is DBState.OneCitySucess -> {
                            Assert.assertThat(it.cityData.id, CoreMatchers.`is`(9))
                            cancel()
                    }

                    else -> {

                    }
                }
            }
        }
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getHome_EntityFavorite() = runBlockingTest {
        val city = City(Coord(12.345, 67.890), "Country", 123, "City", 100000, 123456, 789012, 3600)
        val forecastList = mutableListOf(
            ForeCastData(
                Clouds(0),
                1658886000,
                "2024-03-26 12:00:00",
                Main(25.0, 15, 20, 1902, 30, 22.0, 90.0, 100.0, 10.0),
                10000,
                mutableListOf(Weather("Clear", "01d", 800, "Clear Sky")),
                Wind(10, 2.5, 3.4)
            )
        )
        val favorite = WeatherResponse(
            1,
            city,
            123.456,
            78.90,
            5,
            0,
            0,
            "200",
            forecastList,
            0
        )
        val favorite2 = WeatherResponse(
            2,
            city,
            23.456,
            38.90,
            5,
            0,
            0,
            "200",
            forecastList,
            0
        )

        val expectedAlertData = emptyList<WeatherResponse>()

        homeViewModel.addWeatherDataIntoDB(favorite, 9.0, 77.0)
//        homeViewModel.addWeatherDataIntoDB(favorite2, 23.0, 1.0)

        homeViewModel.removeFromDataBase()
//        homeViewModel.showWeatherDataFromDB()

        launch {
            homeViewModel.favDataHome.collect {
                when (it) {
                    is DBState.OneCitySucess -> {
                        Assert.assertNull(it.cityData)

                        cancel()

                    }
                    else -> {
                    }
                }
            }
        }
    }



//
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun deleteFavorite_EntityFavorite() = runBlockingTest {
//        val city = City(Coord(12.345, 67.890), "Country", 123, "City", 100000, 123456, 789012, 3600)
//        val forecastList = mutableListOf(
//            ForeCastData(
//                Clouds(0),
//                1658886000,
//                "2024-03-26 12:00:00",
//                Main(25.0, 15, 20, 1902, 30, 22.0, 90.0, 100.0, 10.0),
//                10000,
//                mutableListOf(Weather("Clear", "01d", 800, "Clear Sky")),
//                Wind(10, 2.5, 3.4)
//            )
//        )
//        val favorite = WeatherResponse(
//            1,
//            city,
//            123.456,
//            78.90,
//            5,
//            1,
//            0,
//            "200",
//            forecastList,
//            0
//        )
//        val favorite2 = WeatherResponse(
//            2,
//            city,
//            23.456,
//            38.90,
//            5,
//            1,
//            0,
//            "200",
//            forecastList,
//            0
//        )
//
//        val expectedAlertData = emptyList<WeatherResponse>()
//
//        homeViewModel.addToFavorites(favorite, 9.0, 77.0)
//        homeViewModel.addToFavorites(favorite2, 23.0, 1.0)
//
//        homeViewModel.removeFromFavorites(favorite)
//        homeViewModel.showFavItems()
//
//        launch {
//            homeViewModel.currentWeather.collect {
//                when (it) {
//                    is DBState.Suceess -> {
//                        if (it.data.isNotEmpty()) {
//                            Assert.assertThat(it.data[0].id, CoreMatchers.`is`(2))
//                            cancel()
//                        }
//                    }
//
//                    else -> {
//                    }
//                }
//            }
//        }
//    }
}
