package com.ezcall.data.dataSource.remote

import com.ezcall.data.dataSource.remote.entities.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response
import timber.log.Timber

fun <T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> = flow {

    emit(ApiResponse.Loading)
    withTimeoutOrNull(2000) {

        val response = call()
        try {
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ApiResponse.Success(it))
                }
            } else {
                response.errorBody()?.let { error ->
                    error.close()
                    val parsedError: ErrorResponse =
                        Gson().fromJson(error.charStream(), ErrorResponse::class.java)
                    Timber.e(parsedError.message)
                    emit(ApiResponse.Failure(parsedError.message, parsedError.code))
                }
            }
        } catch (e: Exception) {
            Timber.e(e.message)
            emit(ApiResponse.Failure(e.message ?: e.toString(), 400))

        }

    } ?: emit(ApiResponse.Failure("Timeout! Please try again.", 408))


}