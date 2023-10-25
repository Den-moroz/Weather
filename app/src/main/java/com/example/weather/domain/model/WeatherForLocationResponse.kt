package com.example.weather.domain.model

data class WeatherForLocationResponse(
    val timezone: String,
    val days: List<WeatherLocationDay>
)