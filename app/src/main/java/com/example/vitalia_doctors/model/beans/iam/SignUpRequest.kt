package com.example.vitalia_doctors.model.beans.iam

data class SignUpRequest (
    val username: String,
    val password: String,
    val roles: List<String>,
    val emailAddress: String
)