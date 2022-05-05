package com.crystal.weatherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crystal.weatherapp.Const
import com.crystal.weatherapp.WeatherService
import com.crystal.weatherapp.model.ConsolidatedWeather
import com.crystal.weatherapp.model.LocationConsolidateWeatherDto
import com.crystal.weatherapp.model.WeatherLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherViewModel : ViewModel() {
    companion object {
        val TAG: String = this.javaClass.simpleName
    }

    private var _progressStatus = MutableLiveData<Boolean>()
    val progressStatus: LiveData<Boolean>
        get() = _progressStatus

    private val _locationWeatherList = MutableLiveData<List<WeatherLocation>>()
    val locationWeatherList: LiveData<List<WeatherLocation>>
        get() = _locationWeatherList

    private val retrofit = Retrofit.Builder()
        .baseUrl(Const.WEATHER_SERVICE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherService = retrofit.create(WeatherService::class.java)

    fun loadData(progress: Boolean) {
        if (progress) {
            setProgressbar(true)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val execute = weatherService.getLocationList(Const.LOCATION_LIST_QUERY).execute()
            if (execute.isSuccessful) {
                execute.body()?.let {
                    val list = it
                    list.forEach { location ->
                        location.consolidatedWeather = testGetLocationWeather(
                            weatherService,
                            location.woeid.toString()
                        ) ?: mutableListOf()
                    }
                    _locationWeatherList.postValue(list)
                }

            } else {
                Log.e(TAG, "getLocationList Not successful")
            }
            if (progress) {
                setProgressbar(false)
            }
        }

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
                    Log.i(TAG, "getLocationWeather : ${response.body().toString()}")
                    if (!response.isSuccessful) {
                        Log.e(MainActivity.TAG, "getLocationWeather - not successful")
                        return
                    }
                    response.body()?.let { dto ->
                        it.consolidatedWeather = dto.consolidatedWeather

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

    private fun testGetLocationWeather(
        weatherService: WeatherService,
        woeid: String
    ): List<ConsolidatedWeather>? {

        return weatherService.getLocationWeather(woeid).execute().body()?.consolidatedWeather
    }

    fun setProgressbar(visibly: Boolean) {
        _progressStatus.postValue(visibly)
    }
}