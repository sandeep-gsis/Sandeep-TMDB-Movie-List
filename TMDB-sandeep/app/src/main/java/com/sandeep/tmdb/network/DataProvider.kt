package com.sandeep.tmdb.network

import android.content.Context
import com.sandeep.tmdb.model.ResponseMoviesList

object DataProvider {

    var unauthorizedCallback: () -> Unit = {}

    var getContext: () -> Context? = { null }
    suspend fun getMoviesList(page: Int ,apiKey: String): Result<ResponseMoviesList> {
        return makeRequest {
            RestAPI.serviceClient.getMoviesList(page,apiKey)
        }
    }
}