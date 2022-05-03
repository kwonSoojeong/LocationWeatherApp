package com.crystal.weatherapp

import com.crystal.weatherapp.model.LocationConsolidateWeatherDto
import com.crystal.weatherapp.model.WeatherLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {

    //https://www.metaweather.com/api/location/search/?query=se
    @GET("/api/location/search")
    fun getLocationList(
        @Query("query") query: String
    ): Call<List<WeatherLocation>>

    //    https://www.metaweather.com/api/location/1132599/
    @GET("/api/location/{id}")
    fun getLocationWeather(
        @Path("id") id: String
    ): Call<LocationConsolidateWeatherDto>
}