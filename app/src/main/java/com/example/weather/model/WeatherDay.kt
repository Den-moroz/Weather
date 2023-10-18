package com.example.weather.model

data class WeatherDay(
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val humidity: Double,
    val windspeed: Double,
    val conditions: String,
    val icon: String,
    val hours: List<WeatherHour>,
    val precipprob: Double
)