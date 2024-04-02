package com.example.weatherapppoject.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapppoject.MainRule
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.alert.viewmodel.AlertViewModel
import com.example.weatherapppoject.getOrAwaitValue
import com.example.weatherapppoject.repository.FakeRepository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AlertViewModelTest {

@get:Rule
val rule = InstantTaskExecutorRule()

@get:Rule
val main = MainRule()

private lateinit var alertViewModel: AlertViewModel
private lateinit var repository : FakeRepository

@Before
fun getSetup(){
    repository = FakeRepository()
    alertViewModel = AlertViewModel(repository)
}



    @Test
    fun getAlerts(): Unit = runBlocking {
        val alertData = AlertData(
            5,
            "1:00 AM",
            "2023-03-28",
            "6:00 AM",
            "2024-03-19",
            1658828400000L,
            17653L,
            1658886000000L,
            17654L,
            123456,
            "39.456",
            "31.90"
        )
        val expectedAlertData = listOf(alertData)

        alertViewModel.insertOneALert(alertData)
        alertViewModel.getAlerts()
        Assert.assertThat(alertViewModel.alerts.getOrAwaitValue(), CoreMatchers.equalTo(expectedAlertData)
        )

    }
@Test
fun insertIntoAlerts(): Unit = runBlocking {
    val alertData = AlertData(
        1,
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
    val expectedAlertData = listOf(alertData)

    alertViewModel.insertOneALert(alertData)
    alertViewModel.getAlerts()
    Assert.assertThat(alertViewModel.alerts.getOrAwaitValue(), CoreMatchers.equalTo(expectedAlertData)
    )

}

    @Test
    fun DeleteAlert(): Unit = runBlocking {
        val alertData = AlertData(
            1,
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

        alertViewModel.insertOneALert(alertData)
        alertViewModel.deleteOneAlert(alertData)
        val expectedAlertData = emptyList<AlertData>()
        Assert.assertThat(alertViewModel.alerts.getOrAwaitValue(), CoreMatchers.equalTo(expectedAlertData)
        )

    }

}
