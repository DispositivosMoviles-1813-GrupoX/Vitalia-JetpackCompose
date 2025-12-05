package com.example.vitalia_doctors.model.beans.doctor

data class Doctor(
    val id: Long,
    val licenseNumber: String,
    val specialty: String,
    val schedules: List<Schedule>,
    val fullName: FullName,
    val contactInfo: ContactInfo,
    val userId: Long // Campo añadido para enlazar con el usuario
)
