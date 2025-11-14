package com.example.vitalia_doctors.appointment.domain.model

import com.example.vitalia_doctors.appointment.data.dto.AppointmentDto

data class Appointment(
    val appointmentId: Long, // Este ID es obligatorio en el dominio, pero se genera en el backend
    val doctorId: Long,
    val residentId: Long,
    val date: String,
    val time: String,
    val status: String,
    val reason: String?,
    val notes: String?
) {
    fun toDto(): AppointmentDto {
        return AppointmentDto(
            appointmentId = if (appointmentId == 0L) null else appointmentId, // Si es 0, lo enviamos como nulo
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