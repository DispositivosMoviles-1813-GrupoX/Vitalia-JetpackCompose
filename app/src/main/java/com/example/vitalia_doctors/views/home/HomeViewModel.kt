package com.example.vitalia_doctors.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vitalia_doctors.doctor.domain.repository.DoctorRepository
import com.example.vitalia_doctors.model.beans.doctor.Doctor
import com.example.vitalia_doctors.model.beans.resident.Resident
import com.example.vitalia_doctors.model.client.RetrofitClient
import com.example.vitalia_doctors.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val doctor: Doctor? = null,
    val assignedResidents: List<Resident> = emptyList(), // Añadido de nuevo
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(private val doctorRepository: DoctorRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private suspend fun ensureTokenIsReady() {
        while (RetrofitClient.token == null) {
            delay(100)
        }
    }

    fun loadDoctorData(firstName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            ensureTokenIsReady()

            when (val result = doctorRepository.getDoctors()) {
                is Result.Success -> {
                    val doctor = result.data.find { it.fullName.firstName.equals(firstName, ignoreCase = true) }
                    if (doctor != null) {
                        _uiState.update { it.copy(doctor = doctor) } // Actualiza el doctor
                        loadAllResidents() // Carga la lista general de residentes
                    } else {
                        _uiState.update { it.copy(error = "No se encontró un doctor con el nombre '$firstName'", isLoading = false) }
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(error = result.exception.message, isLoading = false) }
                }
            }
        }
    }

    private fun loadAllResidents() {
        viewModelScope.launch {
            ensureTokenIsReady()

            // Asumimos que existe una función en el repositorio para obtener todos los residentes
            when (val result = doctorRepository.getAllResidents()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            assignedResidents = result.data,
                            isLoading = false // La carga termina aquí
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(error = result.exception.message, isLoading = false) }
                }
            }
        }
    }

    class Factory(private val doctorRepository: DoctorRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(doctorRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}