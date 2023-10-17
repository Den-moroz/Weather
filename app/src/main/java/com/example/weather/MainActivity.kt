package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.adapter.HourlyAdapter
import com.example.weather.model.Hourly

class MainActivity : AppCompatActivity() {
    private lateinit var adapterHourly: RecyclerView.Adapter<HourlyAdapter.ViewHolder>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun initRecyclerView() {
        val items = ArrayList<Hourly>()
        items.add(Hourly(hour = "9 pm", temp = 28, picPath = "cloudy"))
        items.add(Hourly(hour = "11 pm", temp = 29, picPath = "sun"))
        items.add(Hourly(hour = "12 pm", temp = 30, picPath = "wind"))
        items.add(Hourly(hour = "1 am", temp = 29, picPath = "rainy"))
        items.add(Hourly(hour = "2 am", temp = 27, picPath = "storm"))

        recyclerView = findViewById(R.id.recycler_weather_every_hour)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterHourly = HourlyAdapter(items)
        recyclerView.adapter = adapterHourly
    }
}