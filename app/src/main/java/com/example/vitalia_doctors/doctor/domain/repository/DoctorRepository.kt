package com.example.vitalia_doctors.doctor.domain.repository

import com.example.vitalia_doctors.doctor.data.dto.DoctorDto
import com.example.vitalia_doctors.doctor.data.dto.UpdateDoctorDto
import com.example.vitalia_doctors.model.beans.doctor.Doctor
import com.example.vitalia_doctors.model.beans.resident.Resident
import com.example.vitalia_doctors.utils.Result

interface DoctorRepository {
    suspend fun getDoctors(): Result<List<Doctor>>
    suspend fun getDoctorById(doctorId: Long): Result<Doctor>
    suspend fun createDoctor(doctor: DoctorDto): Result<Doctor>
    suspend fun updateDoctor(doctorId: Long, doctor: UpdateDoctorDto): Result<Doctor>
    suspend fun getAssignedResidents(doctorId: Long): Result<List<Resident>>
    suspend fun getAllResidents(): Result<List<Resident>> // Nueva función
}
