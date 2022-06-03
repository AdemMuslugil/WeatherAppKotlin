package com.adem.weatherappkotlin2.service

import com.adem.weatherappkotlin2.model.WeatherModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIService {

    val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)



    fun getData(city : String) : Single<WeatherModel>{
        return api.getWeather(city)
    }
}