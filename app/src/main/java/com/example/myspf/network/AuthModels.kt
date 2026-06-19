package com.example.myspf.network

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String?,
    val birth_date: String
)

data class UserResponse(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String?,
    val birth_date: String,
    val phototype: String?,
    val gender: String?,
    val region: String?
)

data class PhototypeUpdateRequest(
    val phototype: String
)

data class PhototypePredictionResponse(
    val phototype: String,
    val confidence: Double
)