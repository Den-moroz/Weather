package com.example.weather.model

data class WeatherForLocationResponse(
    val timezone: String,
    val days: List<WeatherLocationDay>
)