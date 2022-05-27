package com.sandeep.tmdb.model

import  com.sandeep.tmdb.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ApiError(val code: Int, val errorResponse: ErrorResponse) : Throwable(errorResponse.error?.message
        ?: "") {
    companion object {
        fun empty(message: String): ApiError {
            return ApiError(-1, ErrorResponse(message = message))
        }
    }
}

infix fun <T : Any> Result<T>.ifResultSuccess(code: (T) -> Unit) {
    if (this is Result.Success) {
        code.invoke(this.data)
    }
}

suspend infix fun <T : Any> Result<T>.ifResultSuccessSusp(code: suspend (T) -> Unit) {
    if (this is Result.Success) {
        withContext(Dispatchers.Main) {
            code.invoke(this@ifResultSuccessSusp.data)
        }
    }
}

suspend infix fun <T : Any> Result<T>.ifResultSuccessWithoutMain(code: suspend (T) -> Unit) {
    if (this is Result.Success) {
        code.invoke(this.data)
    }
}

fun <T : Any> Result<T>.checkResult(success: (T) -> Unit, error: (Throwable) -> Unit) {
    if (this is Result.Success) {
        success.invoke(this.data)
    } else if (this is Result.Error) {
        error.invoke(this.exception)
    }
}

suspend fun <T : Any> Result<T>.checkResultSuspWithoutMain(success: suspend (T) -> Unit, error: suspend (ApiError) -> Unit, complete: suspend () -> Unit = {}) {
    when (this) {
        is Result.Success -> success.invoke(this.data)
        is Result.Error -> error.invoke(this.exception)
        is Result.Complete -> complete.invoke()
    }
}

suspend fun <T : Any> Result<T>.checkResultSusp(success: suspend (T) -> Unit, error: suspend (ApiError) -> Unit, complete: suspend () -> Unit = {}) {
    when (this) {
        is Result.Success -> withContext(Dispatchers.Main) {
            success.invoke(this@checkResultSusp.data)
        }
        is Result.Error -> withContext(Dispatchers.Main) {
            error.invoke(this@checkResultSusp.exception)
        }
        is Result.Complete -> withContext(Dispatchers.Main) {
            complete.invoke()
        }
    }
}
