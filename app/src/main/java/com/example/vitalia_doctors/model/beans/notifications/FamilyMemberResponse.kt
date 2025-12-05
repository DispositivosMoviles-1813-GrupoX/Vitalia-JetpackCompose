package com.example.vitalia_doctors.model.beans.notifications

import com.google.gson.annotations.SerializedName

data class FamilyMemberResponse(
    val id: Long,

    @SerializedName("fullName")
    val fullName: Name,

    @SerializedName("userId")
    val userId: UserId
) {
    val firstName: String get() = fullName.firstName
    val lastName: String get() = fullName.lastName
}

data class Name(
    val firstName: String,
    val lastName: String
)

data class UserId(
    val value: Long
)