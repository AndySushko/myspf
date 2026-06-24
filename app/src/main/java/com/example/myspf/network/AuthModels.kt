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

data class UvHourlyItem(
    val hour: String,
    val value: Float
)

data class UvDailyItem(
    val day: String,
    val max_uv: Float,
    val hourly: List<UvHourlyItem>
)

data class UvResponse(
    val current_uv: Float,
    val risk_level: String,
    val skin_type: String,
    val max_sun_time: String,
    val hourly: List<UvHourlyItem>,
    val daily: List<UvDailyItem>
)

data class RecommendationItem(
    val type: String,
    val title: String,
    val icon: String
)

data class RecommendationsResponse(
    val recommendations: List<RecommendationItem>
)