package com.example.vitalia_doctors.appointment.data.repository

import com.example.vitalia_doctors.appointment.data.remote.AppointmentApiService
import com.example.vitalia_doctors.model.beans.appointment.Appointment
import com.example.vitalia_doctors.appointment.domain.repository.AppointmentRepository
import com.example.vitalia_doctors.utils.Result

class AppointmentRepositoryImpl(private val apiService: AppointmentApiService) : AppointmentRepository {

    override suspend fun getAppointmentsByDoctorId(doctorId: Long): Result<List<Appointment>> {
        return try {
            val response = apiService.getAppointmentsByDoctorId(doctorId)
            if (response.isSuccessful) {
                Result.Success(response.body()?.map { it.toAppointment() } ?: emptyList())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAppointmentById(id: Long): Result<Appointment> {
        return try {
            val response = apiService.getAppointmentById(id)
            if (response.isSuccessful) {
                Result.Success(response.body()!!.toAppointment())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createAppointment(appointment: Appointment): Result<Unit> {
        return try {
            val response = apiService.createAppointment(appointment.toDto())
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateAppointment(id: Long, appointment: Appointment): Result<Unit> {
        return try {
            val response = apiService.updateAppointment(id, appointment.toDto())
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteAppointment(id: Long): Result<Unit> {
        return try {
            val response = apiService.deleteAppointment(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}