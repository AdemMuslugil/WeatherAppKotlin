package com.adem.weatherappkotlin2.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adem.weatherappkotlin2.model.WeatherModel
import com.adem.weatherappkotlin2.service.WeatherAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.combine

class HomeViewModel : ViewModel() {

    private val weatherAPIService = WeatherAPIService()
    private val disposable = CompositeDisposable()

    val weather = MutableLiveData<WeatherModel>()
    val weatherError = MutableLiveData<Boolean>()
    val weatherLoading = MutableLiveData<Boolean>()


    fun refreshData(city : String){
        getDataFromAPI(city)
    }


    private fun getDataFromAPI(city: String){

        weatherLoading.value = true

        disposable.add(
            weatherAPIService.getData(city)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherModel>(){
                    override fun onSuccess(t: WeatherModel) {
                        showWeather(t)
                    }

                    override fun onError(e: Throwable) {
                        weatherError.value = true
                        println(e.localizedMessage)
                        weatherLoading.value = false

                    }
                }))

    }


    fun showWeather(weatherModel: WeatherModel){
        weather.value = weatherModel
        weatherError.value = false
        weatherLoading.value = false

    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}