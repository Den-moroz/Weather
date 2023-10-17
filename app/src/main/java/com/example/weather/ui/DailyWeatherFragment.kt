package com.example.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.withStateAtLeast
import com.example.weather.R
import com.example.weather.adapter.DailyWeatherAdapter
import com.example.weather.databinding.DailyWeatherBinding

class DailyWeatherFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherAdapter: DailyWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DailyWeatherBinding = DataBindingUtil.inflate(
            inflater, R.layout.daily_weather, container, false
        )

        weatherViewModel.getDailyWeather()
        return binding.root
    }
}