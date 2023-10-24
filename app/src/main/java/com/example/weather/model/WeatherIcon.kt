package com.example.weather.model

import com.example.weather.R

enum class WeatherIcon(val iconCode: String, val iconResourceId: Int) {
    PARTLY_CLOUDY_DAY("partly-cloudy-day", R.drawable.cloudy_sunny),
    CLEAR_DAY("clear-day", R.drawable.sunny),
    RAIN("rain", R.drawable.rainy),
    SNOW("snow", R.drawable.snowy),
    CLOUDY("cloudy", R.drawable.cloudy),
    STORM("storm", R.drawable.storm),
    DEFAULT("windy", R.drawable.windy);

    companion object {
        fun getIconByCode(code: String): WeatherIcon {
            return values().firstOrNull { it.iconCode == code } ?: DEFAULT
        }
    }
}