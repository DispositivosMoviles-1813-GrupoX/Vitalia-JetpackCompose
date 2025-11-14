package com.example.vitalia_doctors.appointment.domain.usecase

import com.example.vitalia_doctors.appointment.domain.repository.AppointmentRepository
import com.example.vitalia_doctors.utils.Result

class DeleteAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return repository.deleteAppointment(id)
    }
}