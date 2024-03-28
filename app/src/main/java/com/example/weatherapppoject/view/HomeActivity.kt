package com.example.weatherapppoject.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.weatherapppoject.R
import com.example.weatherapppoject.alert.view.AlertFragment
import com.example.weatherapppoject.databinding.ActivityHomeBinding
import com.example.weatherapppoject.favorite.view.FavoriteFragment
import com.example.weatherapppoject.home.view.HomeFragment
import com.example.weatherapppoject.settings.SettingsFragment
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.NetworkStateReceiver

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var networkReceiver: NetworkStateReceiver
    private var isNetworkAvailable: Boolean = false
    private lateinit var navController: NavController
    private lateinit var sharedPrefrencesManager: SharedPrefrencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefrencesManager = SharedPrefrencesManager.getInstance(this)
        sharedPrefrencesManager.savelocationChoice(SharedKey.GPS.name, "gps")
        replaceFragment(HomeFragment())

        networkReceiver = NetworkStateReceiver { isConnected ->
            isNetworkAvailable = isConnected
            updateNetworkIndicator()
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            if (isNetworkAvailable) {
                binding.tvNetworkIndicator.visibility = View.GONE

                when (item.itemId) {

                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.Alert -> replaceFragment(AlertFragment())
                    R.id.settings -> replaceFragment(SettingsFragment())
                    R.id.favorite -> replaceFragment(FavoriteFragment())
                }

            } else {
                binding.tvNetworkIndicator.visibility = View.VISIBLE

                when (item.itemId) {

                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.Alert -> replaceFragment(AlertFragment())
                    R.id.settings -> replaceFragment(SettingsFragment())
                    R.id.favorite -> replaceFragment(FavoriteFragment())
                }


            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkReceiver)
    }

    private fun updateNetworkIndicator() {
        if (isNetworkAvailable) {
            binding.tvNetworkIndicator.visibility = View.GONE
        } else {
            binding.tvNetworkIndicator.visibility = View.VISIBLE
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}