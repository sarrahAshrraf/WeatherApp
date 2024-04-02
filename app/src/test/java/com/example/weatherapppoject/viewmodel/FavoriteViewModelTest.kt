package com.example.weatherapppoject.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapppoject.AddressUtil
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
import com.example.weatherapppoject.repository.FakeRepository
import com.example.weatherapppoject.utils.DBState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTestOnTestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FavoriteViewModelTest {

    private val latitude = 37.7749
    private val longitude = -122.4194
    private lateinit var applicationContext: Context
    private lateinit var testDispatcher: TestCoroutineDispatcher
    private lateinit var favViewModel: FavoriteViewModel
    private lateinit var repository: FakeRepository
//    @get:Rule
//    val rule = InstantTaskExecutorRule()

    @get:Rule
    val main = MainRule()

    @Before
    fun getSetup() {
        repository = FakeRepository()
        favViewModel = FavoriteViewModel(repository)
        applicationContext = ApplicationProvider.getApplicationContext()
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun getAddressFromCoordinates_withNoAddress_returnsNoAddressFound() = runTest {
        val addressUtil = AddressUtil()
        val result = addressUtil.getAddressFromCoordinates(latitude, longitude, applicationContext)
        assertEquals("No address found", result)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFavoriteData_insertTwoFavs_returnListOfFavorites()= runTest {

        val city = City(Coord(12.345, 67.890), "Country", 123, "City", 100000, 123456, 789012, 3600)
        val forecastList = mutableListOf(
            ForeCastData(
                Clouds(0),
                1658886000,
                "2024-03-26 12:00:00",
                Main(25.0, 15,20,1902,30,22.0,90.0,100.0,10.0),
                10000,
                mutableListOf(Weather("Clear", "01d", 800, "Clear Sky")),
                Wind(10,2.5,3.4)
            )
        )
        val favorite = WeatherResponse(
            1,
            city,
            123.456,
            78.90,
            5,
            1,
            0,
            "200",
            forecastList,
            0
        )

        favViewModel.addToFavorites(favorite,9.0,77.0)

        favViewModel.showFavItems()

        launch {

            favViewModel.favorite.collect {
                when (it) {
                    is DBState.Suceess -> {
                        if (it.data.isNotEmpty()) {
                            Assert.assertThat(it.data, `is`(listOf(favorite)))
                            cancel()
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteOneFavoriteItem_insertTwoAndDeleteOne_returnIdOfTheUndeletedItem()= runTest {
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
            1,
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
            1,
            0,
            "200",
            forecastList,
            0
        )

        val expectedAlertData = emptyList<WeatherResponse>()

        favViewModel.addToFavorites(favorite, 9.0, 77.0)
        favViewModel.addToFavorites(favorite2, 23.0, 1.0)

        favViewModel.removeFromFavorites(favorite)
        favViewModel.showFavItems()

        launch {
            favViewModel.favorite.collect {
                when (it) {
                    is DBState.Suceess -> {
                        if (it.data.isNotEmpty()) {
                            Assert.assertThat(it.data[0].id, `is`(2))
                            cancel()
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFavoriteDetails_insertOneFavItem_returnDetailsOfThatItem()= runTest {

        val city = City(Coord(12.345, 67.890), "Country", 123, "City", 100000, 123456, 789012, 3600)
        val forecastList = mutableListOf(
            ForeCastData(
                Clouds(0),
                1658886000,
                "2024-03-26 12:00:00",
                Main(25.0, 15,20,1902,30,22.0,90.0,100.0,10.0),
                10000,
                mutableListOf(Weather("Clear", "01d", 800, "Clear Sky")),
                Wind(10,2.5,3.4)
            )
        )
        val favorite = WeatherResponse(
            1,
            city,
            123.456,
            78.90,
            5,
            1,
            0,
            "200",
            forecastList,
            0
        )

        favViewModel.addToFavorites(favorite,9.0,77.0)
        favViewModel.showWeatherDetails(9.0,77.0)

        val job =launch {

            favViewModel.favorite.collect {
                when (it) {
                    is DBState.OneCitySucess -> {
                        Assert.assertThat(it.cityData.id, `is`(favorite.id))
                        Assert.assertThat(it.cityData.city, `is`(favorite.city))
                        Assert.assertThat(it.cityData.latitude, `is`(favorite.latitude))
                        cancel()
                    }
                    else -> {

                    }
                }
            }
        }
    }
}