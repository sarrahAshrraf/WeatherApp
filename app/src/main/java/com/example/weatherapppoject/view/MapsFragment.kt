package com.example.weatherapppoject.view

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.weatherapppoject.R
import com.example.weatherapppoject.databinding.ActivityHomeBinding
import com.example.weatherapppoject.databinding.FragmentHomeBinding
import com.example.weatherapppoject.databinding.FragmentMapsBinding
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.Utils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places



class MapsFragment : Fragment() {
    private lateinit var geocoder: Geocoder
    private lateinit var binding: FragmentMapsBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var sharedPrefrencesManager: SharedPrefrencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefrencesManager = SharedPrefrencesManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Places.initialize(requireContext(), Utils.MAPS_KEY)
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geocoder = Geocoder(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { map ->
            googleMap = map
            googleMap.setOnMapClickListener { latLng ->
                val locationName = getAddressFromLatLng(latLng)
                addMarkerToMap(latLng, locationName)
            }
        }

        binding.imgSearchIcon.setOnClickListener {
            val searchQuery = binding.etSearchMap.text.toString()
            if (searchQuery.isNotEmpty()) {
                searchLocation(searchQuery)
            }
        }
        binding.btnSelectOrAddOnMap.setOnClickListener{
//            binding.btnSelectOrAddOnMap.isEnabled
            replaceFragments(HomeFragment())
            Toast.makeText(requireContext(), "click", Toast.LENGTH_SHORT).show()

        }

        binding.imgClearSearch.setOnClickListener {
           binding.etSearchMap.text.clear()
            googleMap.clear()

        //            searchLocation(emptyQuery.toString())
        }
    }

    private fun searchLocation(locationName: String) {
        val addresses = geocoder.getFromLocationName(locationName, 1)
        if (addresses!!.isNotEmpty()) {
            val address = addresses[0]
            val latLng = LatLng(address.latitude, address.longitude)
            moveCameraToLocation(latLng)
            addMarkerToMap(latLng, locationName)
        } else {
            Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCameraToLocation(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

    private fun addMarkerToMap(latLng: LatLng, title: String) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng).title(title))
        val locationFromMark = getAddressFromLatLng(latLng)
        binding.etSearchMap.setText(locationFromMark)
        sharedPrefrencesManager.saveLocationFromMap(SharedKey.GPS.name, latLng.longitude,latLng.latitude)

    }

    private fun getAddressFromLatLng(latLng: LatLng): String {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        sharedPrefrencesManager.saveLocationFromMap(SharedKey.GPS.name, latLng.longitude,latLng.latitude)
        return if (addresses!!.isNotEmpty()) {
            addresses[0]!!.getAddressLine(0)
        } else {
            "Unknown Location"
        }
    }

    private fun replaceFragments(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
        transaction.commit()
    }
}
