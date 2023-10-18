package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.WeatherResponse
import com.example.weather.network.WeatherApi
import kotlinx.coroutines.launch

const val TAG = "WeatherViewModel";

class WeatherViewModel: ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> get() = _weatherData

    private val weatherApi = WeatherApi.create()


    fun getDailyWeather() {
        viewModelScope.launch {
            try {
                val response = weatherApi.getDailyWeather()
                _weatherData.value = response
            } catch (e: Exception) {
                Log.e(TAG, "Something went wrong when trying to execute network request")
            }
        }
    }
}