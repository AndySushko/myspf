package com.example.myspf.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.Path
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<UserResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<UserResponse>

    @PATCH("users/{user_id}/phototype")
    suspend fun updatePhototype(
        @Path("user_id") userId: Int,
        @Body request: PhototypeUpdateRequest
    ): Response<UserResponse>

    @Multipart
    @POST("ml/predict-phototype")
    suspend fun predictPhototype(
        @Part file: MultipartBody.Part
    ): Response<PhototypePredictionResponse>
}