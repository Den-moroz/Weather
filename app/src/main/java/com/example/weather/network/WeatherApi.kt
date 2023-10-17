package com.example.weather.network

import com.example.weather.model.WeatherResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface WeatherApi {
    @GET("VisualCrossingWebServices/rest/services/timeline/london/today?unitGroup=us&elements=datetime%2Ctempmax%2Ctempmin%2Ctemp%2Chumidity%2Cprecipcover%2Cpreciptype%2Cwindspeed&include=hours%2Cdays%2Cevents&key=FYMKZKFJVW7DAMXF5852SKWHC&contentType=json")
    suspend fun getDailyWeather(): WeatherResponse

    companion object {
        private const val BASE_URL = "https://weather.visualcrossing.com/";

        fun create(): WeatherApi {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return retrofit.create(WeatherApi::class.java)
        }
    }
}