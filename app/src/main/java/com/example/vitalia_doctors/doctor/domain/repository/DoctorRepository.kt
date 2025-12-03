package com.example.vitalia_doctors.doctor.domain.repository

import com.example.vitalia_doctors.doctor.data.dto.DoctorDto
import com.example.vitalia_doctors.doctor.data.dto.UpdateDoctorDto
import com.example.vitalia_doctors.model.beans.doctor.Doctor
import com.example.vitalia_doctors.utils.Result

interface DoctorRepository {
    suspend fun getDoctors(): Result<List<Doctor>>
    suspend fun createDoctor(doctor: DoctorDto): Result<Doctor>
    suspend fun updateDoctor(doctorId: Long, doctor: UpdateDoctorDto): Result<Doctor>
}
