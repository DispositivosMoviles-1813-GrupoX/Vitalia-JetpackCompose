package com.example.vitalia_doctors.appointment.domain.repository

import com.example.vitalia_doctors.appointment.domain.model.Appointment
import com.example.vitalia_doctors.utils.Result

interface AppointmentRepository {
    suspend fun getAppointmentsByDoctorId(doctorId: Long): Result<List<Appointment>>
    suspend fun getAppointmentById(id: Long): Result<Appointment>
    suspend fun createAppointment(appointment: Appointment): Result<Unit>
    suspend fun updateAppointment(id: Long, appointment: Appointment): Result<Unit>
    suspend fun deleteAppointment(id: Long): Result<Unit>
}