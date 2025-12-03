package com.example.vitalia_doctors.appointment.data.dto

import com.example.vitalia_doctors.model.beans.appointment.Appointment

data class AppointmentDto(
    val id: Long? = null,
    val doctorId: Long,
    val residentId: Long,
    val date: String,
    val time: TimeDto,
    val status: String
) {
    fun toAppointment(): Appointment {
        return Appointment(
            id = id ?: 0L,
            doctorId = doctorId,
            residentId = residentId,
            date = date,
            time = "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}",
            status = status
        )
    }
}