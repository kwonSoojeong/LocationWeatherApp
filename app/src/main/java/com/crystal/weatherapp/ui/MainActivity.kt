package com.crystal.weatherapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.crystal.weatherapp.databinding.ActivityMainBinding
import com.crystal.weatherapp.dpToPx
import com.crystal.weatherapp.model.WeatherLocation


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG: String = this.javaClass.simpleName
    }
    private lateinit var binding: ActivityMainBinding

    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        initView()
        setObserver()
    }

    private fun initView() {
        with(binding.swipeRefreshLayout){
            setOnRefreshListener {
                binding.tableLayout.isGone = true
                weatherViewModel.loadData(false)
            }
        }

        weatherViewModel.loadData(true)

    }


    private fun setObserver() {
        weatherViewModel.progressStatus.observe(this) { visibility ->
            binding.progressBar.isGone = !visibility
        }
        weatherViewModel.locationWeatherList.observe(this) {
            setTableLayout(it)
        }
    }

    private fun setTableLayout(data : List<WeatherLocation>) {
        val tableLayout = binding.tableLayout.apply { isGone = false }
        data.forEach {
            setRow(it, tableLayout)
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setRow(data: WeatherLocation, tableLayout: TableLayout) {
        val layoutParam = TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        val px = dpToPx(this,0.5f)
        layoutParam.setMargins(px,px,px,px)

        val row = TableRow(this)
        val textview = TextView(this)
        textview.text = data.title
        textview.setBackgroundColor(Color.WHITE)
        textview.gravity = Gravity.CENTER
        layoutParam.weight = 1.0f
        row.addView(textview, layoutParam)

        if(!data.consolidatedWeather.isNullOrEmpty()){
            layoutParam.weight = 5.0f
            val todayWeather = WeatherItem(data.consolidatedWeather[0], this)
            row.addView(todayWeather, layoutParam)
            val tomorrowWeather = WeatherItem(data.consolidatedWeather[1], this)
            row.addView(tomorrowWeather, layoutParam)
        }
        tableLayout.addView(row)
    }
}