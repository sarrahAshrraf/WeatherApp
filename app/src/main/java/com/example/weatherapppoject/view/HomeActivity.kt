package com.example.weatherapppoject.view

import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

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
        if( sharedPrefrencesManager.getLanguae(SharedKey.LANGUAGE.name,"") =="en"){
            setLocale("en")
        }
        else if ( sharedPrefrencesManager.getLanguae(SharedKey.LANGUAGE.name,"") =="ar"){
            setLocale("ar")
        }
        else{//null
            setLocale("en")
        }
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
        .addToBackStack(null)
        .commit()
}

    private fun setLocale(language: String) {
        val resources = this.resources
        val config = Configuration(resources.configuration)
        val locale = Locale(language)
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        ViewCompat.setLayoutDirection(this.window.decorView, if (language == "ar") ViewCompat.LAYOUT_DIRECTION_RTL else ViewCompat.LAYOUT_DIRECTION_LTR)
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }


}