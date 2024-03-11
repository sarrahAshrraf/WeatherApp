package com.example.weatherapppoject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.weatherapppoject.R
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.weatherapppoject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.animationView.playAnimation()
        Glide.with(this)
            .asGif()
            .load(R.drawable.wordsplashscreen)
            .into(binding.imageView)

        handler.postDelayed({
            binding.animationView.cancelAnimation()
            navigateToHomeActivity() }, 5000)



    }
    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        binding.animationView.resumeAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.animationView.pauseAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.animationView.cancelAnimation()
    }
}