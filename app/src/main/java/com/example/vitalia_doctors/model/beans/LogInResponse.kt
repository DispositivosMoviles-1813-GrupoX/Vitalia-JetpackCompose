package com.example.vitalia_doctors.model.beans

data class LogInResponse (
    val id: Long,
    val username: String,
    val token: String
)