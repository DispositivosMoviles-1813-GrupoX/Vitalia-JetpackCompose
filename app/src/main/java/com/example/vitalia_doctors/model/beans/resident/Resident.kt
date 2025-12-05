package com.example.vitalia_doctors.model.beans.resident

data class Resident(
    val id: Long,
    val dni: String,
    val firstName: String,
    val lastName: String,
    val city: String,
    val state: String,
    val country: String,
    val birthDate: String,
    val gender: String
)
