package com.example.vitalia_doctors.appointment.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalia_doctors.appointment.domain.model.Appointment
import com.example.vitalia_doctors.appointment.domain.usecase.*
import com.example.vitalia_doctors.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val getAppointmentsByDoctorUseCase: GetAppointmentsByDoctorUseCase,
    private val getAppointmentByIdUseCase: GetAppointmentByIdUseCase,
    private val createAppointmentUseCase: CreateAppointmentUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase
) : ViewModel() {

    private val _appointmentsState = MutableStateFlow<Result<List<Appointment>>?>(null)
    val appointmentsState = _appointmentsState.asStateFlow()

    private val _appointmentDetailState = MutableStateFlow<Result<Appointment>?>(null)
    val appointmentDetailState = _appointmentDetailState.asStateFlow()

    fun loadAppointmentsByDoctor(doctorId: Long) {
        viewModelScope.launch {
            _appointmentsState.value = getAppointmentsByDoctorUseCase(doctorId)
        }
    }

    fun getAppointmentDetail(id: Long) {
        viewModelScope.launch {
            _appointmentDetailState.value = getAppointmentByIdUseCase(id)
        }
    }

    fun createAppointment(appointment: Appointment) {
        viewModelScope.launch {
            createAppointmentUseCase(appointment)
        }
    }

    fun updateAppointment(id: Long, appointment: Appointment) {
        viewModelScope.launch {
            updateAppointmentUseCase(id, appointment)
        }
    }

    fun deleteAppointment(id: Long) {
        viewModelScope.launch {
            deleteAppointmentUseCase(id)
        }
    }
}