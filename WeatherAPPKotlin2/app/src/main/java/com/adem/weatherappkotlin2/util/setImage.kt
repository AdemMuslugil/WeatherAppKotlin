package com.adem.weatherappkotlin2.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.downloadIcon(url:String , progressDrawable: CircularProgressDrawable){

    val option = RequestOptions()
        .placeholder(placeholderForImage(context))

    Glide
        .with(context)
        .setDefaultRequestOptions(option)
        .load(url)
        .into(this)

}


fun placeholderForImage(contex: Context) : CircularProgressDrawable{
    return CircularProgressDrawable(contex).apply {
        strokeWidth = 4f
        centerRadius = 20f
        start()
    }
}