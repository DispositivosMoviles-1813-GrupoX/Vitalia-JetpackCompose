package com.example.vitalia_doctors.model.beans

data class Doctor(
    val id: Long,
    val licenseNumber: String,
    val specialty: String,
    val fullName: FullName,
    val contactInfo: ContactInfo,
    val userId: Long
)

data class FullName(
    val firstName: String,
    val lastName: String
)

data class ContactInfo(
    val phone: String,
    val address: Address
)

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String
)