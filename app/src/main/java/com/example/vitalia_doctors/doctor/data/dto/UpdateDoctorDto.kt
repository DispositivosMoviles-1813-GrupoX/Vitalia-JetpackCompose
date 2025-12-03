package com.example.vitalia_doctors.doctor.data.dto

/**
 * DTO for updating a doctor's profile. Excludes fields that are not updatable, like userId.
 */
data class UpdateDoctorDto(
    val licenseNumber: String,
    val specialty: String,
    val fullName: FullNameDto,
    val contactInfo: ContactInfoDto
)
