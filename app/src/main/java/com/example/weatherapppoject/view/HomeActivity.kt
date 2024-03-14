package com.example.weatherapppoject.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.weatherapppoject.R
import com.example.weatherapppoject.databinding.ActivityHomeBinding
import com.example.weatherapppoject.utils.NetworkManager

class HomeActivity : AppCompatActivity(){
    private lateinit var binding: ActivityHomeBinding
    private lateinit var networkReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragments(HomeFragment())

        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                updateNetworkIndicator()
            }
        }
        if (NetworkManager.isNetworkAvailable(this)) {
            binding.tvNetworkIndicator.visibility = View.GONE
        }else {
            binding.tvNetworkIndicator.visibility = View.VISIBLE
        }
        
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragments(HomeFragment())
                R.id.notification -> replaceFragments(NotificationFragment())
                R.id.settings -> replaceFragments(SettingsFragment())
                R.id.favorite -> replaceFragments(FavoriteFragment())
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
        if (NetworkManager.isNetworkAvailable(this)) {
            binding.tvNetworkIndicator.visibility = View.GONE
        } else {
            binding.tvNetworkIndicator.visibility = View.VISIBLE
        }
    }
    private fun replaceFragments(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }

//    override fun onNetworkStatusChanged(isConnected: Boolean) {
//        runOnUiThread {
//            if(isConnected){
//                binding.tvNetworkIndicator.visibility = View.GONE
//            } else{
//                binding.tvNetworkIndicator.visibility = View.VISIBLE
//            }
//        }
//    }
}