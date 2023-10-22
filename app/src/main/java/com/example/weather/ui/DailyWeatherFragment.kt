package com.example.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.adapter.HourlyAdapter
import com.example.weather.data.DataStoreManager
import com.example.weather.databinding.DailyWeatherBinding
import com.example.weather.service.WeatherApplication
import com.example.weather.service.WeatherViewModel
import com.example.weather.service.WeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyWeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by activityViewModels {
        WeatherViewModelFactory(
            (activity?.application as WeatherApplication).database.locationDao(), DataStoreManager(requireContext())
        )
    }

    private lateinit var dataStoreManager: DataStoreManager

    private lateinit var adapterHourly: RecyclerView.Adapter<HourlyAdapter.ViewHolder>
    private lateinit var recyclerView: RecyclerView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            getUserLocation()
        } else {
            Toast.makeText(requireContext(), R.string.toast_location_permission_required, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DailyWeatherBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.location.observe(viewLifecycleOwner, Observer {
            viewModel.getDailyWeather()
        })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.changeLocation.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                viewModel.updateLocation(binding.changeLocation.text.toString())

                binding.changeLocation.text = null
                binding.changeLocation.isEnabled = false
                binding.changeLocation.clearFocus()

                binding.changeLocation.isEnabled = true

                true
            } else {
                false
            }
        }

        viewModel.dailyWeatherData.observe(viewLifecycleOwner, Observer { weatherResponse ->
            weatherResponse?.let {
                val refactoredTimezone = it.timezone.replace("/", " | ").replace("_", " ")
                binding.location.text = refactoredTimezone
                val iconResource = when (it.days[0].icon) {
                    "partly-cloudy-day" -> R.drawable.cloudy_sunny
                    "clear-day" -> R.drawable.sunny
                    "rain" -> R.drawable.rainy
                    "snow" -> R.drawable.snowy
                    "cloudy" -> R.drawable.cloudy
                    "storm" -> R.drawable.storm
                    else -> R.drawable.windy
                }
                Glide.with(requireContext())
                    .load(iconResource)
                    .into(binding.currentWeatherImage)
                val currentDateTime = LocalDateTime.now();
                val formattedDate = currentDateTime.format(DateTimeFormatter.ofPattern("EEE MMM d"));
                val formattedTime = currentDateTime.format(DateTimeFormatter.ofPattern("h:mm a"));
                binding.date.setText(getString(R.string.label_datetime, formattedDate, formattedTime));
                binding.temperature.text = getString(R.string.label_temperature, it.days[0].temp.toString())
                binding.currentMaxAndMinTemperature.text = getString(
                    R.string.label_temp_max_and_min,
                    Math.round(it.days[0].tempmax).toString(),
                    Math.round(it.days[0].tempmin).toString()
                )
                binding.humidityText.text = getString(R.string.label_humidity, it.days[0].humidity.toString())
                binding.rainText.text = getString(R.string.label_rain, it.days[0].precipprob.toString())
                binding.windSpeedText.text = getString(R.string.label_wind_speed, it.days[0].windspeed.toString())

                binding.nextDayTitle.setOnClickListener {
                    findNavController().navigate(R.id.action_dailyWeatherFragment_to_weeklyWeatherFragment)
                }

                binding.savedLocationImage.setOnClickListener {
                    findNavController().navigate(R.id.action_dailyWeatherFragment_to_locationsFragment)
                }

                recyclerView = binding.root.findViewById(R.id.recycler_weather_every_hour)
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                val hoursOfCurrentDay = it.days[0].hours.subList(currentDateTime.hour, it.days[0].hours.size)
                val hoursOfNextDay = it.days[1].hours
                adapterHourly = HourlyAdapter((hoursOfCurrentDay + hoursOfNextDay).subList(0, 24))
                recyclerView.adapter = adapterHourly
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStoreManager = DataStoreManager(requireContext())

        dataStoreManager.locationFlow.onEach { savedLocation ->
            if (savedLocation.isNotBlank()) {
                viewModel.updateLocation(savedLocation)
            } else {
                requestLocationPermission()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getUserLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val userLocation = "${it.latitude},${it.longitude}"
                    viewModel.updateLocation(userLocation)

                    viewModel.insertLocation(com.example.weather.data.Location(locationName = userLocation))
                    viewModel.getDailyWeather()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), R.string.toast_location_permission_required, Toast.LENGTH_SHORT).show()
            }
    }
}
