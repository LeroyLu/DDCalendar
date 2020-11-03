package com.leroylu.struct.util.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author jiaj.lu
 * @date 2020/8/19
 * @description
 */
object Client {

    lateinit var retrofit: Retrofit

    var host: String = ""

    fun init(url: String, client: OkHttpClient = HttpClientBuilder.build()) {
        this.host = url
        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}