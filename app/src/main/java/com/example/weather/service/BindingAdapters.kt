package com.example.weather.service

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.weather.R
import com.example.weather.service.WeatherApiStatus

class BindingAdapters {
}

@BindingAdapter("internetImageBinding")
fun bindStatus(imageView: ImageView,
               status: WeatherApiStatus?) {
    when (status) {
        WeatherApiStatus.LOADING -> {
            imageView.visibility = View.VISIBLE
        }
        WeatherApiStatus.ERROR -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_connection_error)
        }
        else -> {
            imageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("constraintBinding")
fun bindStatusForController(constraintLayout: ConstraintLayout, status: WeatherApiStatus?) {
    when (status) {
        WeatherApiStatus.LOADING -> {
            constraintLayout.findViewById<ConstraintLayout>(R.id.constraint_main).visibility = View.GONE
            constraintLayout.findViewById<TextView>(R.id.internet_problem_text).visibility = View.GONE
            constraintLayout.findViewById<TextView>(R.id.internet_problem_title).visibility = View.GONE
            constraintLayout.findViewById<ImageView>(R.id.status_image).visibility = View.GONE
        }
        WeatherApiStatus.ERROR -> {
            constraintLayout.findViewById<ConstraintLayout>(R.id.constraint_main).visibility = View.GONE
            constraintLayout.findViewById<TextView>(R.id.internet_problem_text).visibility = View.VISIBLE
            constraintLayout.findViewById<TextView>(R.id.internet_problem_title).visibility = View.VISIBLE
            constraintLayout.findViewById<ImageView>(R.id.status_image).visibility = View.VISIBLE
        }
        else -> {
            constraintLayout.visibility = View.VISIBLE
            constraintLayout.findViewById<ConstraintLayout>(R.id.constraint_main).visibility = View.VISIBLE
        }
    }
}
