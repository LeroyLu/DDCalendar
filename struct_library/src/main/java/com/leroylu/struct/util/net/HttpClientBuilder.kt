package com.leroylu.struct.util.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * @author jiaj.lu
 * @date 2020/8/18
 * @description
 */
object HttpClientBuilder {

    fun build(
        unit: TimeUnit = TimeUnit.SECONDS,
        conn: Long = 10,
        read: Long = 10,
        write: Long = 10,
        vararg interceptor: Interceptor = arrayOf()
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.connectTimeout(conn, unit)
            .readTimeout(read, unit)
            .writeTimeout(write, unit)

        for (item in interceptor) {
            builder.addInterceptor(item)
        }

        return builder.build()
    }
}