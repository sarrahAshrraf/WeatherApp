package com.example.weatherapppoject.alert

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapppoject.R
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FragmentFavoriteBinding
import com.example.weatherapppoject.databinding.FragmentNotificationBinding
import com.example.weatherapppoject.favorite.view.FavoriteDetailsFragment
import com.example.weatherapppoject.favorite.view.FavoritesAdapter
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.ALertDBState
import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.utils.OneCallState
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch




class NotificationFragment : Fragment() {
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSourceInte: LocalDataSourceInte
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var alertViewModelFactory: AlertViewModelFactory
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    private lateinit var alertsAdapter: AlertsAdapter
    private lateinit var alertRecyclerView: RecyclerView
    private lateinit var alertLayoutManager: LinearLayoutManager
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository = WeatherRepositoryImpl.getInstance(remoteDataSource, localDataSourceInte)

        alertViewModelFactory = AlertViewModelFactory(repository)
        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)
        alertLayoutManager = LinearLayoutManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.alertsRecView.layoutManager = alertLayoutManager

        alertsAdapter = AlertsAdapter(emptyList(),
            { product, position ->
                // Delete confirmation dialog
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setTitle("Delete Confirmation")
                alertDialogBuilder.setMessage("Are you sure you want to remove this item?")
                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                    alertViewModel.removeFromALerts(product)
                    dialog.dismiss()
                }
                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            },
            { product, position ->
                // On card click listener
                Toast.makeText(requireContext(), "on card", Toast.LENGTH_SHORT).show()
            }
        )

        binding.alertsRecView.adapter = alertsAdapter
        alertViewModel.showAlertsItems()
        lifecycleScope.launch(Dispatchers.Main) {

            alertViewModel.AlertData.collect { state ->
                when (state) {
                    is ALertDBState.Loading -> {
                        Toast.makeText(requireContext(),"loadi ",Toast.LENGTH_SHORT).show()

                    }

                    is ALertDBState.Suceess -> {
                        val alertData = state.alertdata
                        alertsAdapter.submitList(alertData)
                        if (alertData.isEmpty()) {
                            binding.animationView.visibility = View.VISIBLE
                            binding.AddAlertfloatingActionButton.visibility = View.GONE
                        } else {
                            binding.animationView.visibility = View.GONE
                        }
                    }

                    else -> {
                        Toast.makeText(requireContext(),"else ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Click listener for FloatingActionButton
//        binding.AddAlertfloatingActionButton.setOnClickListener {
//            // Perform network call and insert data into the database
//            lifecycleScope.launch {
//                try {
//                    val weatherResponse = alertViewModel.makeNetworkCall()
//                    if (weatherResponse != null) {
//                        alertViewModel.addToAlerts(weatherResponse, weatherResponse.lon, weatherResponse.lat)
//                        Log.i("===db add", "onViewCreated: ")
//                    }
//                } catch (e: Exception) {
//                    // Handle network call or insertion error
//                    Log.e("===error in Alerts", "onViewCreated: ", e)
//                }
//            }
//        }
    }
}







//class NotificationFragment : Fragment() {
//    private lateinit var floatingActionButton : FloatingActionButton
//    private lateinit var remoteDataSource: RemoteDataSource
//    private lateinit var localDataSourceInte: LocalDataSourceInte
//    private lateinit var alertViewModel: AlertViewModel
//    private lateinit var repository: WeatherRepositoryImpl
//    private lateinit var alertViewModelFactory: AlertViewModelFactory
////    private lateinit var homeViewModel: HomeFragmentViewModel
////    private lateinit var homeFactory : HomeFragmentViewModelFactory
//    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
//    private lateinit var alertsAdapter: AlertsAdapter
//    private lateinit var alertRecyclerView: RecyclerView
//    private lateinit var alertLayoutManager: LinearLayoutManager
//    private lateinit var binding : FragmentNotificationBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
//        remoteDataSource = RemoteDataSourceImp()
//        localDataSourceInte = LocalDataSourceImp(requireContext())
//        repository= WeatherRepositoryImpl.getInstance(remoteDataSource,localDataSourceInte)
//
//        alertViewModelFactory = AlertViewModelFactory(repository)
//        alertViewModel = ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)
//        alertLayoutManager = LinearLayoutManager(requireContext())
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentNotificationBinding.inflate(inflater, container, false)
//        return binding.root
////        return inflater.inflate(R.layout.fragment_notification, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//
//        binding.alertsRecView.layoutManager = alertLayoutManager
//
//
//        alertsAdapter = AlertsAdapter(emptyList(),
//            { product, position ->
//                val alertDialogBuilder = AlertDialog.Builder(context)
//                alertDialogBuilder.setTitle("Delete Confirmation")
//                alertDialogBuilder.setMessage("Are you sure you want to remove this item?")
//                alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
//                    alertViewModel.removeFromALerts(product)
//                    dialog.dismiss()
//                }
//                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.dismiss()
//                }
//                val alertDialog = alertDialogBuilder.create()
//                alertDialog.show()
//            },
//            { product, position ->
//
//                // Navigate to the new fragment
////
////                    val bundle = Bundle().apply {
////
////                        putDoubleArray("longlat", doubleArrayOf(product.longitude, product.latitude))
////                    }
////                    // Navigate to the new fragment
////                    val fragment = FavoriteDetailsFragment()
////                    fragment.arguments = bundle
////                    parentFragmentManager.beginTransaction()
////                        .replace(R.id.container, fragment)
////                        .addToBackStack(null)
////                        .commit()
//
////                replaceFragments(FavoriteDetailsFragment())
//                Toast.makeText(requireContext(),"on card", Toast.LENGTH_SHORT).show()
//            }
//        )
//
//        binding.alertsRecView.adapter = alertsAdapter
//        alertViewModel.showAlertsItems()
//
//        binding.AddAlertfloatingActionButton.setOnClickListener {
//            //network call
//
//            lifecycleScope.launch(Dispatchers.Main) {
//
//             alertViewModel.alertsData.collectLatest { weatherResponse ->
//              when (weatherResponse) {
//            is OneCallState.Suceess -> {
//
//                Log.i("==home fragment alert", ""+weatherResponse.data.alerts)
//                Log.i("==home fragment alert", ""+weatherResponse.data.current)
//
//                CoroutineScope(Dispatchers.IO).launch {
////                                db.getWeatherDAO().setAsFavorite(weatherResponse.data,weatherResponse.data.city.coord.lon,weatherResponse.data.city.coord.lat)
//                    alertViewModel.addToAlerts(
//                        weatherResponse.data,
//                        weatherResponse.data.lon,
//                        weatherResponse.data.lat
//                    )
//                    Log.i("===db add", "onViewCreated: ")
//                }
//
////                        repository.insertAlertIntoDB(weatherResponse,weatherResponse.data.lon,weatherResponse.data.lat)
//            }
//
//            is OneCallState.Loading -> {
//
//                Log.i("===lodaing in Alerts", "onViewCreated: ")
//
//            }
//
//            else -> {
//
//                Log.i("===error in Alerts", "onViewCreated: ")
//
//            }
//        }
//    }
//
//
//
//                lifecycleScope.launch(Dispatchers.Main) {
////            favoriteViewModel.showFavItems()
//
//                    alertViewModel.AlertData.collect { state ->
//                        when (state) {
//                            is ALertDBState.Loading -> {
////                       binding.animationView.visibility =View.VISIBLE
//                            }
//
//                            is  ALertDBState.Suceess -> {
//                                // Update the adapter with the new data
////                        binding.animationView.visibility =View.GONE
//
//                                alertsAdapter.submitList(state.alertdata)
//                                if(alertsAdapter.itemCount==0){
//                                    binding.animationView.visibility =View.VISIBLE
//                                }
//                                else {  binding.animationView.visibility =View.GONE}
//                            }
//
//                            else -> {}
//                        }
//                    }
//                }
//
//
//}
//
//
//
//
//
//            //get from database -> from adapter
//
//        }
//
//
//
//    }
//
//
//
//}












////todo move to alert fragment
//lifecycleScope.launch(Dispatchers.Main) {
//
//    viewModel.alertsData.collectLatest { weatherResponse ->
//        when (weatherResponse) {
//            is OneCallState.Suceess -> {
//
//                Log.i("==home fragment alert", ""+weatherResponse.data.alerts)
//                Log.i("==home fragment alert", ""+weatherResponse.data.current)
//
//                CoroutineScope(Dispatchers.IO).launch {
////                                db.getWeatherDAO().setAsFavorite(weatherResponse.data,weatherResponse.data.city.coord.lon,weatherResponse.data.city.coord.lat)
//                    viewModel.addToAlerts(
//                        weatherResponse.data,
//                        weatherResponse.data.lon,
//                        weatherResponse.data.lat
//                    )
//                    Log.i("===db add", "onViewCreated: ")
//                }
//
////                        repository.insertAlertIntoDB(weatherResponse,weatherResponse.data.lon,weatherResponse.data.lat)
//            }
//
//            is OneCallState.Loading -> {
//
//                Log.i("===lodaing in Alerts", "onViewCreated: ")
//
//            }
//
//            else -> {
//
//                Log.i("===error in Alerts", "onViewCreated: ")
//
//            }
//        }
//    }
//
//
//}