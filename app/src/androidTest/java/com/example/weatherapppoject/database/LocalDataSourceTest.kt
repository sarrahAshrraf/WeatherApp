package com.example.weatherapppoject.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.forecastmodel.City
import com.example.weatherapppoject.forecastmodel.Clouds
import com.example.weatherapppoject.forecastmodel.Coord
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.Main
import com.example.weatherapppoject.forecastmodel.Weather
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.forecastmodel.Wind
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest {

    private lateinit var database: AppDB
    private lateinit var concreteLocalSource: LocalDataSourceImp

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDB::class.java)
            .allowMainThreadQueries()
            .build()

        concreteLocalSource = LocalDataSourceImp(context)
    }

    @After
    fun deleteDatabase() = database.close()

    @Test
    fun insertAlertAndGetAlert() = runBlockingTest {
        val alertData = AlertData(
            null,
            "9:00 AM",
            "2024-03-28",
            "6:00 PM",
            "2024-03-29",
            1658828400000L,
            17653L,
            1658886000000L,
            17654L,
            123456,
            "123.456",
            "78.90"
        )

        concreteLocalSource.insertAlert(alertData)

        val insertedAlert = concreteLocalSource.displayAllAlerts().first().firstOrNull()

        assertNotNull(insertedAlert)
        insertedAlert?.let {
            assertNotNull(it.id)
            assertEquals(alertData.fromTime, it.fromTime)
            assertEquals(alertData.fromDate, it.fromDate)
            assertEquals(alertData.toTime, it.toTime)
            assertEquals(alertData.toDate, it.toDate)
            assertEquals(alertData.milleTimeFrom, it.milleTimeFrom)
            assertEquals(alertData.milleDateFrom, it.milleDateFrom)
            assertEquals(alertData.milleTimeTo, it.milleTimeTo)
            assertEquals(alertData.milleDateTo, it.milleDateTo)
            assertEquals(alertData.requestCode, it.requestCode)
            assertEquals(alertData.lontitude, it.lontitude)
            assertEquals(alertData.lattiude, it.lattiude)
        }

        concreteLocalSource.deleteAlertData(insertedAlert!!)
    }

    @Test
    fun displayAllAlerts() = runBlockingTest {
        val alerts = listOf(
            AlertData(
                null,
                "9:00 AM",
                "2024-03-28",
                "6:00 PM",
                "2024-03-29",
                1658828400000L,
                17653L,
                1658886000000L,
                17654L,
                123456,
                "123.456",
                "78.90"
            ),
            AlertData(
                null,
                "10:00 AM",
                "2024-03-28",
                "7:00 PM",
                "2024-03-29",
                1658832000000L,
                17653L,
                1658889600000L,
                17654L,
                789012,
                "12.34",
                "56.78"
            )
        )

        alerts.forEach { alert ->
            concreteLocalSource.insertAlert(alert)
        }

        val allAlerts = concreteLocalSource.displayAllAlerts().first()

        assertEquals(alerts.size, allAlerts.size)
        alerts.forEachIndexed { index, expectedAlert ->
            val actualAlert = allAlerts[index]
            assertNotNull(actualAlert.id)
            assertEquals(expectedAlert.fromTime, actualAlert.fromTime)
            assertEquals(expectedAlert.fromDate, actualAlert.fromDate)
            assertEquals(expectedAlert.toTime, actualAlert.toTime)
            assertEquals(expectedAlert.toDate, actualAlert.toDate)
            assertEquals(expectedAlert.milleTimeFrom, actualAlert.milleTimeFrom)
            assertEquals(expectedAlert.milleDateFrom, actualAlert.milleDateFrom)
            assertEquals(expectedAlert.milleTimeTo, actualAlert.milleTimeTo)
            assertEquals(expectedAlert.milleDateTo, actualAlert.milleDateTo)
            assertEquals(expectedAlert.requestCode, actualAlert.requestCode)
            assertEquals(expectedAlert.lontitude, actualAlert.lontitude)
            assertEquals(expectedAlert.lattiude, actualAlert.lattiude)
        }
    }


    @Test
    fun deleteAlertData() = runBlockingTest {
        val alertData = AlertData(
            null,
            "9:00 AM",
            "2024-03-28",
            "6:00 PM",
            "2024-03-29",
            1658828400000L,
            17653L,
            1658886000000L,
            17654L,
            123456,
            "123.456",
            "78.90"
        )

        concreteLocalSource.insertAlert(alertData)

        val insertedAlert = concreteLocalSource.displayAllAlerts().first().firstOrNull()
        assertNotNull(insertedAlert)

        concreteLocalSource.deleteAlertData(insertedAlert!!)
        val remainingAlerts = concreteLocalSource.displayAllAlerts().first()

        assertTrue(remainingAlerts.isEmpty())
    }


    /////=======================================================>
    @Test
    fun setFavoriteData() = runBlockingTest {
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

        concreteLocalSource.setFavoriteData(favorite, favorite.longitude, favorite.latitude)

        val favoriteWeatherFlow = concreteLocalSource.displayAllFav()

        assertNotNull(favoriteWeatherFlow)

        val favoriteWeatherList = favoriteWeatherFlow.first()

        assertEquals(listOf(favorite), favoriteWeatherList)
    }

    @Test
    fun deleteFavData() = runBlockingTest {
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

        concreteLocalSource.setFavoriteData(favorite, favorite.longitude, favorite.latitude)

        concreteLocalSource.deleteFavData(favorite)

        val favoriteWeather = concreteLocalSource.displayAllFav().first()

        assertTrue(favoriteWeather.isEmpty())
    }
}
