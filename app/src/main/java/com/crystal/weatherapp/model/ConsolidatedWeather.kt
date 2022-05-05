package com.crystal.weatherapp.model

import com.google.gson.annotations.SerializedName

class ConsolidatedWeather(
    @SerializedName("id")
    val id: Long,

    @SerializedName("weather_state_name")
    var weatherStateName: String,

    @SerializedName("weather_state_abbr")
    var weatherStateAbbr: String,

    @SerializedName("wind_direction_compass")
    var windDirectionCompass: String,

    @SerializedName("created")
    var created: String,

    @SerializedName("applicable_date")
    var applicableDate: String,

    @SerializedName("min_temp")
    var minTemp: Float,

    @SerializedName("max_temp")
    var maxTemp: Float,

    @SerializedName("the_temp")
    var theTemp: Float,

    @SerializedName("wind_speed")
    var windSpeed: Float,

    @SerializedName("wind_direction")
    var windDirection: Float,

    @SerializedName("air_pressure")
    var airPressure: Float,

    @SerializedName("humidity")
    var humidity: Int,

    @SerializedName("visibility")
    var visibility: Float,

    @SerializedName("predictability")
    var predictability: Int
)
