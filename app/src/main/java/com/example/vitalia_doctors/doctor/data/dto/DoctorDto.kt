package com.example.vitalia_doctors.doctor.data.dto

data class DoctorDto(
    val licenseNumber: String,
    val specialty: String,
    val fullName: FullNameDto,
    val contactInfo: ContactInfoDto,
    val userId: Long
)
