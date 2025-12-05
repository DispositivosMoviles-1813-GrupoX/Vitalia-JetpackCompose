package com.example.vitalia_doctors.doctor.domain.repository

import com.example.vitalia_doctors.doctor.data.dto.DoctorDto
import com.example.vitalia_doctors.doctor.data.dto.UpdateDoctorDto
import com.example.vitalia_doctors.doctor.data.remote.DoctorApiService
import com.example.vitalia_doctors.model.beans.doctor.Doctor
import com.example.vitalia_doctors.model.beans.resident.Resident
import com.example.vitalia_doctors.utils.Result

class DoctorRepositoryImpl(private val apiService: DoctorApiService) : DoctorRepository {

    override suspend fun getDoctors(): Result<List<Doctor>> {
        return try {
            val response = apiService.getDoctors()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getDoctorById(doctorId: Long): Result<Doctor> {
        return try {
            val response = apiService.getDoctorById(doctorId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error(Exception("Doctor not found"))
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun createDoctor(doctor: DoctorDto): Result<Doctor> {
        return try {
            val response = apiService.createDoctor(doctor)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateDoctor(doctorId: Long, doctor: UpdateDoctorDto): Result<Doctor> {
        return try {
            val response = apiService.updateDoctor(doctorId, doctor)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAssignedResidents(doctorId: Long): Result<List<Resident>> {
        return try {
            val response = apiService.getAssignedResidents(doctorId)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAllResidents(): Result<List<Resident>> {
        return try {
            val response = apiService.getAllResidents()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}
