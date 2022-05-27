package com.sandeep.tmdb.network


import com.sandeep.tmdb.BuildConfig
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object OkHttpClientProvider {

    fun get(): OkHttpClient = client
    const val HEADER_AUTHORIZATION = "Authorization"

    private val client by lazy {

        val logging = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }

        fun providesRetrofit(gson: Gson, okHttpClient: OkHttpClient) : Retrofit =
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()
    }
}