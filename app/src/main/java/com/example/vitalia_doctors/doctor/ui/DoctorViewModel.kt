package com.example.vitalia_doctors.doctor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vitalia_doctors.doctor.data.dto.DoctorDto
import com.example.vitalia_doctors.doctor.data.dto.UpdateDoctorDto
import com.example.vitalia_doctors.doctor.domain.repository.DoctorRepository
import com.example.vitalia_doctors.model.beans.doctor.Doctor
import com.example.vitalia_doctors.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoctorViewModel(private val repository: DoctorRepository) : ViewModel() {

    private val _doctorState = MutableStateFlow<Result<Doctor?>?>(null)
    val doctorState: StateFlow<Result<Doctor?>?> = _doctorState

    fun findDoctorProfile(firstName: String) {
        viewModelScope.launch {
            when (val result = repository.getDoctors()) {
                is Result.Success -> {
                    val doctor = result.data.find { it.fullName.firstName.equals(firstName, ignoreCase = true) }
                    _doctorState.value = Result.Success(doctor) // doctor puede ser null si no se encuentra
                }
                is Result.Error -> {
                    // Para errores de red, también consideramos que no se encontró un perfil.
                    _doctorState.value = Result.Success(null)
                }
            }
        }
    }

    fun createDoctor(doctor: DoctorDto) {
        viewModelScope.launch {
            val result = repository.createDoctor(doctor)
            // Tras crear, volvemos a buscar para tener el estado actualizado
            if (result is Result.Success) {
                findDoctorProfile(doctor.fullName.firstName)
            } else {
                _doctorState.value = result as Result.Error
            }
        }
    }

    fun updateDoctor(doctorId: Long, doctor: UpdateDoctorDto) {
        viewModelScope.launch {
            val result = repository.updateDoctor(doctorId, doctor)
            if (result is Result.Success) {
                // Actualizar el estado local con el doctor actualizado
                _doctorState.value = result
            } else {
                _doctorState.value = result as Result.Error
            }
        }
    }

    class DoctorViewModelFactory(private val repository: DoctorRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DoctorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DoctorViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}