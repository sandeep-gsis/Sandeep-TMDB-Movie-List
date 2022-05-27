package com.sandeep.tmdb.network

import com.sandeep.tmdb.BuildConfig
import com.sandeep.tmdb.model.ResponseMoviesList
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object RestAPI {
    private val BASE_URL = BuildConfig.BASEURL
    private val okHttpClient = OkHttpClientProvider.get()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonProvider.get())).build()

    @JvmSuppressWildcards
    interface RestAPI {
        @GET("movie")
        suspend fun getMoviesList(
            @Query("page") page: Int,
            @Query("api_key") apiKey: String
        ) : Response<ResponseMoviesList>


    }

    val serviceClient = retrofit.create(RestAPI::class.java)
}