package com.example.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.adapter.HourlyAdapter
import com.example.weather.databinding.DailyWeatherBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyWeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by activityViewModels()

    private lateinit var adapterHourly: RecyclerView.Adapter<HourlyAdapter.ViewHolder>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DailyWeatherBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getDailyWeather()

        viewModel.dailyWeatherData.observe(viewLifecycleOwner, Observer { weatherResponse ->
            weatherResponse?.let {
                binding.currentWeather.text = it.days[0].conditions
                val iconResource = when (it.days[0].icon) {
                    "partly-cloudy-day" -> R.drawable.cloudy_sunny
                    "clear-day" -> R.drawable.sunny
                    "rain" -> R.drawable.rain
                    "snow" -> R.drawable.snowy
                    "cloudy" -> R.drawable.cloudy
                    "storm" -> R.drawable.storm
                    else -> R.drawable.windy
                }
                Glide.with(requireContext())
                    .load(iconResource)
                    .into(binding.currentWeatherImage)
                val currentDateTime = LocalDateTime.now();
                val formattedDate = currentDateTime.format(DateTimeFormatter.ofPattern("EEEE MMMM d"));
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

                recyclerView = binding.root.findViewById(R.id.recycler_weather_every_hour)
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                val hourlyDataFromCurrentHour = it.days[0].hours.subList(currentDateTime.hour, it.days[0].hours.size)
                adapterHourly = HourlyAdapter(hourlyDataFromCurrentHour)
                recyclerView.adapter = adapterHourly
            }
        })
        return binding.root
    }
}