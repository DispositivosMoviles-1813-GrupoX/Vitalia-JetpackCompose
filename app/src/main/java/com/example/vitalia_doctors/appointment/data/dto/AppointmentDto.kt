package com.example.vitalia_doctors.appointment.data.dto

import com.example.vitalia_doctors.appointment.domain.model.Appointment

data class AppointmentDto(
    val appointmentId: Long? = null,
    val doctorId: Long,
    val residentId: Long,
    val date: String,
    val time: String,
    val status: String,
    val reason: String?,
    val notes: String?
) {
    fun toAppointment(): Appointment {
        return Appointment(
            appointmentId = appointmentId ?: 0L, // Si es nulo, usamos 0 como valor por defecto en el dominio
            doctorId = doctorId,
            residentId = residentId,
            date = date,
            time = time,
            status = status,
            reason = reason,
            notes = notes
        )
    }
}