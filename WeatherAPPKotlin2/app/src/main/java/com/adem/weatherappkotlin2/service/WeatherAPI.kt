package com.adem.weatherappkotlin2.service

import com.adem.weatherappkotlin2.model.WeatherModel
import com.adem.weatherappkotlin2.util.API_KEY
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {



    @GET("weather?&units=metric")
    fun getWeather(@Query("q") city : String,
                   @Query("appid") apiKey : String = API_KEY
    ) : Single<WeatherModel>

}