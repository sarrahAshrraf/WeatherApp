package com.example.weatherapppoject.favorite.view

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.example.weatherapppoject.R
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FragmentFavoriteBinding
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.map.view.MapsFragment
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.view.HomeActivity

import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
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
        favAdapter = FavoritesAdapter(emptyList(),
            { product, position ->
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setTitle("Delete Confirmation")
                alertDialogBuilder.setMessage("Are you sure you want to remove this item?")
                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                favoriteViewModel.removeFromFavorites(product)
                    dialog.dismiss()
                }
                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            },
            { product, position ->
                val bundle = Bundle().apply {
                    putDoubleArray("longlat", doubleArrayOf(product.longitude, product.latitude))
                }
                // Navigate to the new fragment
                val fragment = FavoriteDetailsFragment()
                fragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()

                Toast.makeText(requireContext(),"on card",Toast.LENGTH_SHORT).show()
            }
        )







        //        favAdapter = FavoritesAdapter(emptyList()) { data ->
        //            favoriteViewModel.removeFromFavorites(data)
        //
        //        }
        favRecyclerView.adapter = favAdapter
        favRecyclerView.layoutManager = favLayoutManager
        favoriteViewModel.showFavItems()

        //todo intialize the adapter and the list // 3andy mvvm products lab 3ayza ashofo
        lifecycleScope.launch(Dispatchers.Main) {
//            favoriteViewModel.showFavItems()

            favoriteViewModel.favorite.collect { state ->
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


        floatingActionButton.setOnClickListener {
            Log.i("==set Onclcik===", "")
            sharedPreferencesManager.setMap(SharedKey.MAP.name,"fav")
                if(checkForInternet(requireContext())){
                    replaceFragments(MapsFragment())
                }else {

                    Toast.makeText(requireContext(),getString(R.string.networkstatus),Toast.LENGTH_SHORT).show()

                }

        }


    }

private fun replaceFragments(fragment: Fragment) {
    val transaction = (context as HomeActivity).supportFragmentManager.beginTransaction()
    transaction.replace(R.id.frameLayout, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
}

    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}
//val longlat = sharedPreferencesManager.getLocationFromMap(SharedKey.FAV.name)
//val longg = longlat!!.first
//val latt = longlat.second
//Log.i("==latttt longggg===", ""+ longg+ latt)
