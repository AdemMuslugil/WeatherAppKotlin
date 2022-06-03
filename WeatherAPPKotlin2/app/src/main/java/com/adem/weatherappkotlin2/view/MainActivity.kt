package com.adem.weatherappkotlin2.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.adem.weatherappkotlin2.R
import com.adem.weatherappkotlin2.databinding.ActivityMainBinding
import com.adem.weatherappkotlin2.util.downloadIcon
import com.adem.weatherappkotlin2.util.placeholderForImage
import com.adem.weatherappkotlin2.viewModel.HomeViewModel
import com.bumptech.glide.Glide
import java.lang.System.load
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private lateinit var viewModel: HomeViewModel
    private lateinit var  sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val city = sharedPreferences.getString("city", "london")

        viewModel = ViewModelProvider(this)[HomeViewModel :: class.java]
        viewModel.refreshData(city!!)

        searchCity()

        swipeRefresh(city)

        observeLiveData()
    }



    private fun observeLiveData(){

        viewModel.weather.observe(this, Observer {
            it?.let {
                binding.searchCardView.visibility = View.VISIBLE
                binding.cityCardView.visibility = View.VISIBLE
                binding.linearLayoutInfo.visibility = View.VISIBLE
                binding.errorText.visibility = View.GONE
                binding.progressBar.visibility = View.GONE

                binding.feelsText.text = "${it.main!!.feelsLike} °C"
                binding.cityText.text = it.name
                binding.tempText.text = "${it.main.temp.toString()} °C"
                binding.maxTempText.text = "${it.main.tempMax} °C"
                binding.minTempText.text = "${it.main.tempMin} °C"
                binding.humidityText.text = "${it.main.humidity} %"
                binding.hPaText.text = "${it.main.pressure} hPa"

                val speed = convert(it.wind!!.speed!!)
                binding.windSpeedText.text = "$speed KM/H"

                val url = "https://openweathermap.org/img/wn/${it.weather!![0].icon.toString()}@2x.png"
                binding.stateImageView.downloadIcon(url, placeholderForImage(binding.root.context))

            }

        })


        viewModel.weatherError.observe(this, Observer {

            it?.let {
                if (it){
                    binding.progressBar.visibility = View.GONE
                    binding.errorText.visibility = View.VISIBLE
                    binding.searchCardView.visibility = View.VISIBLE
                    binding.cityCardView.visibility = View.GONE
                    binding.linearLayoutInfo.visibility = View.GONE
                }else{
                    binding.errorText.visibility = View.GONE
                    binding.searchCardView.visibility = View.VISIBLE
                    binding.cityCardView.visibility = View.VISIBLE
                    binding.linearLayoutInfo.visibility = View.VISIBLE
                }

            }

        })


        viewModel.weatherLoading.observe(this, Observer {

            it?.let{
                if (it){
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorText.visibility = View.GONE
                    binding.searchCardView.visibility = View.VISIBLE
                    binding.cityCardView.visibility = View.GONE
                    binding.linearLayoutInfo.visibility = View.GONE
                }else{
                    binding.progressBar.visibility = View.GONE
                    binding.searchCardView.visibility = View.VISIBLE
                    binding.cityCardView.visibility = View.VISIBLE
                    binding.linearLayoutInfo.visibility = View.VISIBLE

                }
            }

        })

    }


    private fun swipeRefresh(city :String){

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.searchCardView.visibility = View.GONE
            binding.cityCardView.visibility = View.GONE
            binding.linearLayoutInfo.visibility = View.GONE
            viewModel.refreshData(city)
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }


    private fun convert(speed: Double): String { // -> m/s to km/h
        val df = DecimalFormat("#")
        val speed = df.format(speed * 3.6)
        return speed

    }


    fun searchCity(){

        binding.serchImageButton.setOnClickListener {

            val cityName = binding.searchCityText.text.toString()

            if (cityName.isNotEmpty()){
                sharedPreferences.edit().putString("city", cityName).apply()
            }
            else{
                Toast.makeText(this, "Enter City Name", Toast.LENGTH_SHORT).show()
            }


            val city = sharedPreferences.getString("city","london")
            viewModel.refreshData(city!!)
            swipeRefresh(city)


        }
    }
}