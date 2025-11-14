package com.example.vitalia_doctors.model.response

import retrofit2.http.Query
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
    @GET("notifications/userId/{userId}")
    suspend fun getNotifications(
        @Path("userId") userId: Long
    ): Response<List<NotificationResponse>>

    @GET("notifications/search")
    suspend fun getNotificationByStatus(
        @Query("status") status: String
    ): Response<List<NotificationResponse>>

    @GET("notifications/userId/{userId}/status")
    suspend fun getNotificationsByUserIdAndStatus(
        @Path("userId") userId: Long,
        @Query("status") status: String
    ): Response<List<NotificationResponse>>

}