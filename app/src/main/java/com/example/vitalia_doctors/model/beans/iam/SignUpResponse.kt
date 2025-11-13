package com.example.vitalia_doctors.model.beans.iam

data class SignUpResponse (
    val id: Long,
    val username: String,
    val roles: List<String>,
    val emailAddress: String
)