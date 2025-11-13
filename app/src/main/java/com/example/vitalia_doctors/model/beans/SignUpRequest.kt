package com.example.vitalia_doctors.model.beans

data class SignUpRequest (
    val username: String,
    val password: String,
    val roles: List<String>,
    val emailAddress: String
)