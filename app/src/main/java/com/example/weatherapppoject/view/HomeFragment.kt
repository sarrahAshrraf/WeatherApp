package com.example.weatherapppoject.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weatherapppoject.R
import com.example.weatherapppoject.databinding.FragmentHomeBinding
import com.example.weatherapppoject.databinding.FragmentSettingsBinding
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locale = Locale(sharedPreferencesManager.getString(SharedKey.LANGUAGE.name, "default"))
        Locale.setDefault(locale)
        val resources = requireContext().resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        val context= ContextUtils.wrapContext(requireContext(), locale)

        binding.city.setText(context.getString(R.string.city))
        binding.textDays.setText(context.getString(R.string.language))
        binding.tvPressure.setText(context.getString(R.string.pressure))
        binding.textDays.setText(context.getString(R.string.next5days))
        binding.tvTemp.setText(context.getString(R.string.weathertemp))
        binding.tvHumidity.setText(context.getString(R.string.humidity))
        binding.tvclouds.setText(context.getString(R.string.clouds))
        binding.tvDayFormat.setText(context.getString(R.string.dayformat))
        binding.tvStatus.setText(context.getString(R.string.weatherstatus))
    }

}
