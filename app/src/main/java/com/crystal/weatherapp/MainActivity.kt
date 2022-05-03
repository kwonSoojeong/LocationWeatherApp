package com.crystal.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.crystal.weatherapp.databinding.ActivityMainBinding
import com.crystal.weatherapp.model.LocationConsolidateWeatherDto
import com.crystal.weatherapp.model.WeatherLocation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationWeatherList: List<WeatherLocation>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        loadData()
        initView()
    }

    private fun loadData() {
        setProgressbar(true)
        val retrofit = Retrofit.Builder()
            .baseUrl(Const.WEATHER_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WeatherService::class.java)

        weatherService.getLocationList(Const.LOCATION_LIST_QUERY).enqueue(
            object : Callback<List<WeatherLocation>> {
                override fun onResponse(
                    call: Call<List<WeatherLocation>>,
                    response: Response<List<WeatherLocation>>
                ) {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "getLocationList not successful")
                        setProgressbar(false)
                        return
                    }

                    Log.i(TAG, "getLocationList : ${response.body().toString()}")
                    response.body()?.let {
                        locationWeatherList = it

                        locationWeatherList.forEach { location ->
                            getLocationWeather(weatherService, location)
                        }
                    }
                    setProgressbar(false)
                }

                override fun onFailure(call: Call<List<WeatherLocation>>, t: Throwable) {
                    Log.e(TAG, "getLocationList : ${t.message.toString()}")
                    setProgressbar(false)
                }
            }
        )


    }

    private fun getLocationWeather(
        weatherService: WeatherService,
        it: WeatherLocation
    ) {
        weatherService.getLocationWeather(it.woeid.toString()).enqueue(
            object : Callback<LocationConsolidateWeatherDto> {
                override fun onResponse(
                    call: Call<LocationConsolidateWeatherDto>,
                    response: Response<LocationConsolidateWeatherDto>
                ) {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "getLocationWeather - not successful")
                        return
                    }
                    response.body()?.let { dto ->
                        it.consolidatedWeather = dto.consolidatedWeather
                        Log.i(TAG, response.body()!!.toString())
                    }
                }

                override fun onFailure(
                    call: Call<LocationConsolidateWeatherDto>,
                    t: Throwable
                ) {
                    Log.e(TAG, "getLocationWeather - ${t.message.toString()}")
                }

            }
        )
    }

    private fun initView() {


    }

    private fun setProgressbar(visibility: Boolean) {
        binding.progressBar.isGone = !visibility
    }

    companion object {
        const val TAG = "TEST"
    }
}