package com.example.myspf.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.myspf.R
import com.example.myspf.location.getCurrentLocation
import com.example.myspf.network.RecommendationItem
import com.example.myspf.network.RetrofitClient
import com.example.myspf.session.UserSession
import com.example.myspf.ui.theme.*

@Composable
fun RecommendationsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var recommendations by remember { mutableStateOf<List<RecommendationItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            isLoading = false
            errorText = "Для актуальных рекомендаций разрешите доступ к геолокации на странице УФ-индекса"
            return@LaunchedEffect
        }

        try {
            isLoading = true
            errorText = ""

            val location = getCurrentLocation(context)

            val userLatitude = location?.latitude ?: 45.0355
            val userLongitude = location?.longitude ?: 38.9753

            val response = RetrofitClient.api.getRecommendations(
                latitude = userLatitude,
                longitude = userLongitude,
                phototype = UserSession.phototype ?: "I-II"
            )

            if (response.isSuccessful) {
                recommendations = response.body()?.recommendations ?: emptyList()
            } else {
                errorText = "Не удалось получить рекомендации"
            }
        } catch (e: Exception) {
            errorText = "Ошибка подключения к серверу"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = ScreenHorizontalPadding)
    ) {
        Spacer(modifier = Modifier.height(52.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.icon_back_arrow),
                contentDescription = "Назад",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBackClick() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Защита кожи",
                fontSize = 30.sp,
                color = BlackText,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        when {
            isLoading -> {
                Text(
                    text = "Загрузка рекомендаций...",
                    color = InputPlaceholder,
                    fontSize = 18.sp
                )
            }

            errorText.isNotBlank() -> {
                Text(
                    text = errorText,
                    color = Color(0xFFC96F6F),
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
            }

            recommendations.isEmpty() -> {
                Text(
                    text = "Сейчас защита не требуется",
                    color = BlackText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            else -> {
                recommendations.forEach { recommendation ->
                    RecommendationCard(
                        recommendation = recommendation
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun RecommendationCard(
    recommendation: RecommendationItem
) {
    val iconRes = when (recommendation.icon) {
        "icon_sunscreen" -> R.drawable.icon_sunscreen
        "icon_cap" -> R.drawable.icon_cap
        "icon_bottle_of_water" -> R.drawable.icon_bottle_of_water
        "icon_sunglasses" -> R.drawable.icon_sunglasses
        "icon_house" -> R.drawable.icon_house
        else -> R.drawable.icon_sunscreen
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(Color.White, RoundedCornerShape(26.dp))
            .padding(horizontal = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = recommendation.title,
                modifier = Modifier.size(58.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = recommendation.title,
                color = Color(0xFFBDBDBD),
                fontSize = 22.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}