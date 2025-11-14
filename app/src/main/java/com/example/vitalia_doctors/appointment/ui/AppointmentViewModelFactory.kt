package com.example.vitalia_doctors.appointment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vitalia_doctors.appointment.domain.usecase.*

class AppointmentViewModelFactory(
    private val getAppointmentsByDoctorUseCase: GetAppointmentsByDoctorUseCase,
    private val getAppointmentByIdUseCase: GetAppointmentByIdUseCase,
    private val createAppointmentUseCase: CreateAppointmentUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppointmentViewModel(
                getAppointmentsByDoctorUseCase,
                getAppointmentByIdUseCase,
                createAppointmentUseCase,
                updateAppointmentUseCase,
                deleteAppointmentUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}