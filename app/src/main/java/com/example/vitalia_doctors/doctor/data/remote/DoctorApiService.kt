package com.example.vitalia_doctors.doctor.data.remote

import com.example.vitalia_doctors.doctor.data.dto.DoctorDto
import com.example.vitalia_doctors.doctor.data.dto.UpdateDoctorDto
import com.example.vitalia_doctors.model.beans.doctor.Doctor
import com.example.vitalia_doctors.model.beans.resident.Resident
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DoctorApiService {

    @GET("doctors")
    suspend fun getDoctors(): Response<List<Doctor>>

    @POST("doctors")
    suspend fun createDoctor(@Body doctor: DoctorDto): Response<Doctor>

    @GET("doctors/{id}")
    suspend fun getDoctorById(@Path("id") doctorId: Long): Response<Doctor>

    @PUT("doctors/{id}")
    suspend fun updateDoctor(@Path("id") doctorId: Long, @Body doctor: UpdateDoctorDto): Response<Doctor>

    @GET("doctors/{id}/residents")
    suspend fun getAssignedResidents(@Path("id") doctorId: Long): Response<List<Resident>>

    @GET("residents")
    suspend fun getAllResidents(): Response<List<Resident>>

}
