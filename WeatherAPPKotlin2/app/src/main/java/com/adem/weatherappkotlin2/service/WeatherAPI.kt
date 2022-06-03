package com.adem.weatherappkotlin2.service

import com.adem.weatherappkotlin2.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    //https://api.openweathermap.org/data/2.5/weather?q=london&appid=78766a1bdf46e5335be21f8f5614e330&units=metric

    @GET("weather?&appid=78766a1bdf46e5335be21f8f5614e330&units=metric")
    fun getWeather(@Query("q") city : String
    ) : Single<WeatherModel>

}