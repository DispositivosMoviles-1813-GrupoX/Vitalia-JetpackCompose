package com.example.vitalia_doctors.model.response

import com.example.vitalia_doctors.model.beans.iam.LogInRequest
import com.example.vitalia_doctors.model.beans.iam.LogInResponse
import com.example.vitalia_doctors.model.beans.iam.SignUpRequest
import com.example.vitalia_doctors.model.beans.iam.SignUpResponse
import com.example.vitalia_doctors.model.beans.notifications.NotificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebService {

    // Iam - Services
    @POST("authentication/sign-up")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<SignUpResponse>

    @POST("authentication/sign-in")
    suspend fun signIn(
        @Body request: LogInRequest
    ): Response<LogInResponse>

    // Notification - Services
    @RequiresAuth
    @GET("notifications/userId/{userId}")
    suspend fun getNotifications(
        @Path("userId") userId: Long
    ): Response<List<NotificationResponse>>

}