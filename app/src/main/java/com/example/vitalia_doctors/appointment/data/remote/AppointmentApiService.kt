package com.example.vitalia_doctors.appointment.data.remote

import com.example.vitalia_doctors.appointment.data.dto.AppointmentDto
import retrofit2.Response
import retrofit2.http.*

interface AppointmentApiService {

    @GET("appointments/searchByDoctorId")
    suspend fun getAppointmentsByDoctorId(@Query("doctorId") doctorId: Long): Response<List<AppointmentDto>>

    @GET("appointments/{appointmentId}")
    suspend fun getAppointmentById(@Path("appointmentId") appointmentId: Long): Response<AppointmentDto>

    @POST("appointments")
    suspend fun createAppointment(@Body appointment: AppointmentDto): Response<AppointmentDto>

    @PUT("appointments/{appointmentId}")
    suspend fun updateAppointment(@Path("appointmentId") appointmentId: Long, @Body appointment: AppointmentDto): Response<AppointmentDto>

    @DELETE("appointments/{appointmentId}")
    suspend fun deleteAppointment(@Path("appointmentId") appointmentId: Long): Response<Unit>
}