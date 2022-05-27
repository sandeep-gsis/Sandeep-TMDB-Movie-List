package com.sandeep.tmdb.network

import com.sandeep.tmdb.model.ApiError
import com.sandeep.tmdb.model.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.Executors


val dbScope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())

inline fun <reified T : Any> makeRequest(operation: () -> Response<T>): Result<T> {
    return try {
        val response = operation()
        if (response.isSuccessful) {
            if (response.code() == 204 || response.body() == null) {
                throw EmptyDataException(response.headers())
            }
            Result.Success(response.body() ?: throw EmptyDataException(response.headers()))
        } else {
                val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                if (error != null) {
                    Result.Error(ApiError(response.code(), error))
                } else {
                    Result.Error(
                            ApiError(
                                    response.code(),
                                    ErrorResponse(message = "Http error ${response.code()}")
                            )
                    )
                }
        }
    } catch (e: AuthorizationError) {
        Result.Error(ApiError(-1, ErrorResponse(message = e.message)))
    } catch (e: EmptyDataException) {
        Result.Complete(e.headers)
    } catch (e: HttpException) {
        // Catch http errors
        Result.Error(
                ApiError(
                        e.code(),
                        ErrorResponse(code = e.code().toString(), message = e.message)
                )
        )
    } catch (e: Throwable) {
        Result.Error(ApiError(-2, ErrorResponse(message = e.message)))
    }
}