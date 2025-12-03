package com.example.vitalia_doctors.model.beans.appointment

import com.example.vitalia_doctors.appointment.data.dto.AppointmentDto
import com.example.vitalia_doctors.appointment.data.dto.TimeDto

data class Appointment(
    val id: Long,
    val doctorId: Long,
    val residentId: Long,
    val date: String,
    val time: String, // e.g., "14:30"
    val status: String
) {
    fun toDto(): AppointmentDto {
        val timeParts = time.split(":").map { it.toInt() }
        val timeDto = TimeDto(
            hour = timeParts.getOrElse(0) { 0 },
            minute = timeParts.getOrElse(1) { 0 },
            second = 0,
            nano = 0
        )
        return AppointmentDto(
            id = if (id == 0L) null else id,
            doctorId = doctorId,
            residentId = residentId,
            date = date,
            time = timeDto,
            status = status
        )
    }
}