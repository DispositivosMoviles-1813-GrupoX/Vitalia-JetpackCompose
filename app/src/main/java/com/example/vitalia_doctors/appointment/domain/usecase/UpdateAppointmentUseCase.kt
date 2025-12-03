package com.example.vitalia_doctors.appointment.domain.usecase

import com.example.vitalia_doctors.appointment.domain.repository.AppointmentRepository
import com.example.vitalia_doctors.model.beans.appointment.Appointment
import com.example.vitalia_doctors.utils.Result

class UpdateAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(id: Long, appointment: Appointment): Result<Unit> {
        return repository.updateAppointment(id, appointment)
    }
}