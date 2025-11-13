package com.example.vitalia_doctors.model.beans

data class SignUpResponse (
    val id: Long,
    val username: String,
    val roles: List<String>,
    val emailAddress: String
)