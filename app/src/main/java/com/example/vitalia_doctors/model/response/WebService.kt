package com.example.vitalia_doctors.model.response

import com.example.vitalia_doctors.model.beans.LogInRequest
import com.example.vitalia_doctors.model.beans.LogInResponse
import com.example.vitalia_doctors.model.beans.SignUpRequest
import com.example.vitalia_doctors.model.beans.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WebService {

    @POST("authentication/sign-up")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<SignUpResponse>

    @POST("authentication/sign-in")
    suspend fun signIn(
        @Body request: LogInRequest
    ): Response<LogInResponse>

}