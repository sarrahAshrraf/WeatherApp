package com.example.weatherapppoject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.weatherapppoject.R
import com.bumptech.glide.Glide
import com.example.weatherapppoject.databinding.ActivityMainBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler()
    private var isFirstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isFirstLaunch = savedInstanceState == null
        if (isFirstLaunch) {
            binding.animationView.playAnimation()
            Glide.with(this)
                .asGif()
                .load(R.drawable.wordsplashscreen)
                .into(binding.imageView)
            handler.postDelayed({
                binding.animationView.cancelAnimation()
                navigateToHomeActivity()
            }, 5000)
        } else {
            navigateToHomeActivity()
        }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLaunch) {
            binding.animationView.resumeAnimation()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isFirstLaunch) {
            binding.animationView.pauseAnimation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFirstLaunch) {
            binding.animationView.cancelAnimation()
        }
    }
}