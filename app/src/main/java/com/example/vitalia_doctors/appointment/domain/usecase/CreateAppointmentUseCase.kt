package com.example.vitalia_doctors.appointment.domain.usecase

import com.example.vitalia_doctors.appointment.domain.model.Appointment
import com.example.vitalia_doctors.appointment.domain.repository.AppointmentRepository
import com.example.vitalia_doctors.utils.Result

class CreateAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(appointment: Appointment): Result<Unit> {
        return repository.createAppointment(appointment)
    }
}