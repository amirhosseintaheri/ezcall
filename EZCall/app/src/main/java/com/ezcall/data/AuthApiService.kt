package com.ezcall.data

import com.ezcall.data.dataSource.remote.entities.AuthLogin
import com.ezcall.data.dataSource.remote.entities.AuthSignUp
import com.ezcall.data.dataSource.remote.entities.LoginResponse
import com.ezcall.data.dataSource.remote.entities.SignUpResponse
import com.ezcall.data.dataSource.remote.entities.TokenVerify
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login/")
    suspend fun loginUser(@Body authLogin: AuthLogin): Response<LoginResponse>

    @POST("users/register/")
    suspend fun signUpUser(@Body authSignUp: AuthSignUp): Response<SignUpResponse>

    @POST("auth/verify/")
    suspend fun verifyToken(@Body token: TokenVerify): Response<LoginResponse>

    @POST("auth/refresh/")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<LoginResponse>

}