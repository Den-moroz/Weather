package com.example.weather.network

import com.example.weather.model.WeatherResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface WeatherApi {
    @GET("VisualCrossingWebServices/rest/services/timeline/dubai/today?unitGroup=metric&elements=datetime%2CdatetimeEpoch%2Ctempmax%2Ctempmin%2Ctemp%2Chumidity%2Cprecipprob%2Cwindspeed%2Cconditions%2Cicon&include=hours%2Cdays&key=FYMKZKFJVW7DAMXF5852SKWHC&contentType=json")
    suspend fun getDailyWeather(): WeatherResponse

    @GET("VisualCrossingWebServices/rest/services/timeline/dubai/next7days?unitGroup=metric&elements=datetime%2CdatetimeEpoch%2Ctempmax%2Ctempmin%2Ctemp%2Chumidity%2Cprecipprob%2Cwindspeed%2Cconditions%2Cicon&include=days%2Chours&key=FYMKZKFJVW7DAMXF5852SKWHC&contentType=json")
    suspend fun getWeeklyWeather(): WeatherResponse

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