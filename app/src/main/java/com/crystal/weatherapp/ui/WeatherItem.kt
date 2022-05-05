package com.crystal.weatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crystal.weatherapp.Const
import com.crystal.weatherapp.databinding.ItemWeatherBinding
import com.crystal.weatherapp.model.ConsolidatedWeather

class WeatherItem(val weather: ConsolidatedWeather?, context: Context) : ConstraintLayout(context) {
    private val binding: ItemWeatherBinding =
        ItemWeatherBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setView()
    }

    @SuppressLint("SetTextI18n")
    private fun setView() {
        binding.weather.text = weather?.weatherStateName//weather_state_name
        binding.temp.text = "${weather?.theTemp?.toInt()}°C"
        binding.humidity.text = "${weather?.humidity}%"

        Glide.with(this)
            .load("${Const.WEATHER_SERVICE_URL}/static/img/weather/png/${weather?.weatherStateAbbr}.png")
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.image)
    }
}