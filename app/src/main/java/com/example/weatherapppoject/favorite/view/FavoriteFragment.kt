package com.example.weatherapppoject.favorite.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.example.weatherapppoject.R
import com.example.weatherapppoject.database.AppDB
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FavoriteItemBinding
import com.example.weatherapppoject.databinding.FragmentFavoriteBinding
import com.example.weatherapppoject.databinding.FragmentHomeBinding
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.map.view.MapsFragment
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.utils.ApiState
import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.view.FiveDaysAdapter
import com.example.weatherapppoject.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.viewmodel.HomeFragmentViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private lateinit var floatingActionButton : FloatingActionButton
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSourceInte: LocalDataSourceInte
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var viewModelFactory: FavoriteViewModelFactory
    private lateinit var homeViewModel: HomeFragmentViewModel
    private lateinit var homeFactory : HomeFragmentViewModelFactory
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    private lateinit var favAdapter : FavoritesAdapter
    private lateinit var favRecyclerView: RecyclerView
    private lateinit var favLayoutManager: LinearLayoutManager
    private lateinit var binding : FragmentFavoriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository= WeatherRepositoryImpl.getInstance(remoteDataSource,localDataSourceInte)

        viewModelFactory = FavoriteViewModelFactory(repository)
        favoriteViewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)

        homeFactory = HomeFragmentViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, homeFactory).get(HomeFragmentViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingActionButton  = view.findViewById(R.id.floatingActionButton)
        favRecyclerView = view.findViewById(R.id.favRecView)
        favLayoutManager = LinearLayoutManager(requireContext())
//        favAdapter = FavoritesAdapter(emptyList()) // Pass an empty list initially

        favAdapter = FavoritesAdapter(emptyList()) { product ->
            favoriteViewModel.removeFromFavorites(product)
        }
        favRecyclerView.adapter = favAdapter
        favRecyclerView.layoutManager = favLayoutManager
        favoriteViewModel.showFavItems()

        //todo intialize the adapter and the list // 3andy mvvm products lab 3ayza ashofo
        lifecycleScope.launch(Dispatchers.Main) {
//            favoriteViewModel.showFavItems()

            favoriteViewModel.currentWeather.collect { state ->
                when (state) {
                    is DBState.Loading -> {
//                       binding.animationView.visibility =View.VISIBLE
                    }

                    is DBState.Suceess -> {
                        // Update the adapter with the new data
//                        binding.animationView.visibility =View.GONE

                        favAdapter.submitList(state.data)
                        if(favAdapter.itemCount==0){
                            binding.animationView.visibility =View.VISIBLE
                        }
                        else {  binding.animationView.visibility =View.GONE}
                    }

                    else -> {}
                }
            }
        }





        val longlat = sharedPreferencesManager.getLocationFromMap(SharedKey.GPS.name)
        val longg = longlat!!.first
        val latt = longlat.second
        Log.i("==latttt longggg===", ""+ longg+ latt)


        floatingActionButton.setOnClickListener {
//            val db = Room.databaseBuilder(requireContext(), AppDB::class.java, "rr").build()
            Log.i("==set Onclcik===", "")

            replaceFragments(MapsFragment())

        }


    }
    private fun replaceFragments(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}