package com.crystal.weatherapp.model

import com.google.gson.annotations.SerializedName

data class LocationConsolidateWeatherDto(
    @SerializedName("consolidated_weather")
    val consolidatedWeather: List<ConsolidatedWeather>,

    @SerializedName("title")
    val title: String,

    @SerializedName("woied")
    val woied: Int
)
