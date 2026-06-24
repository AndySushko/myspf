package com.example.myspf.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.myspf.R
import com.example.myspf.location.getCurrentLocation
import com.example.myspf.network.RetrofitClient
import com.example.myspf.network.UvResponse
import com.example.myspf.session.UserSession
import com.example.myspf.ui.theme.*

@Composable
fun UvIndexScreen(
    onBackClick: () -> Unit,
    onRecommendationsClick: () -> Unit
) {
    val context = LocalContext.current

    var selectedDay by remember { mutableStateOf(0) }
    var uvData by remember { mutableStateOf<UvResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorText by remember { mutableStateOf("") }
    var permissionGranted by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted

        if (!isGranted) {
            errorText = "Для точного УФ-индекса разрешите доступ к геолокации"
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            permissionGranted = true
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(permissionGranted) {
        if (!permissionGranted) return@LaunchedEffect

        try {
            isLoading = true
            errorText = ""

            val location = getCurrentLocation(context)

            val userLatitude = location?.latitude ?: 45.0355
            val userLongitude = location?.longitude ?: 38.9753

            val response = RetrofitClient.api.getUv(
                latitude = userLatitude,
                longitude = userLongitude,
                phototype = UserSession.phototype ?: "I-II"
            )

            if (response.isSuccessful) {
                uvData = response.body()
            } else {
                errorText = "Не удалось получить данные УФ-индекса"
            }
        } catch (e: Exception) {
            errorText = "Ошибка подключения к серверу"
        } finally {
            isLoading = false
        }
    }

    val selectedHourly = uvData?.daily
        ?.getOrNull(selectedDay)
        ?.hourly

    val values = selectedHourly?.map { it.value }
        ?: listOf(0.4f, 0.8f, 1.6f, 2.3f, 2.1f, 1.4f, 0.7f, 0.2f)

    val hours = selectedHourly?.map { it.hour }
        ?: listOf("6", "8", "10", "12", "14", "16", "18", "20")

    val days = uvData?.daily?.map { it.day }
        ?: listOf("Сегодня", "Завтра", "Ср", "Чт", "Пт", "Сб", "Вс")

    val selectedUv = uvData?.daily?.getOrNull(selectedDay)?.max_uv
        ?: uvData?.current_uv
        ?: 2.3f

    val phototype = UserSession.phototype ?: "I-II"

    val selectedRiskLevel = calculateRiskLevel(selectedUv)
    val selectedMaxSunTime = calculateMaxSunTime(selectedUv, phototype)

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
                text = "УФ-индекс",
                fontSize = 30.sp,
                color = BlackText,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        if (isLoading) {
            Text(
                text = "Загрузка данных...",
                color = InputPlaceholder,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        if (errorText.isNotBlank()) {
            Text(
                text = errorText,
                color = Color(0xFFC96F6F),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        UvChartCard(
            values = values,
            hours = hours
        )

        Spacer(modifier = Modifier.height(14.dp))

        SmallUvCard(
            currentUv = selectedUv,
            riskLevel = selectedRiskLevel,
            skinType = "$phototype тип",
            maxSunTime = selectedMaxSunTime
        )

        Spacer(modifier = Modifier.height(14.dp))

        DaySelector(
            days = days,
            selectedDay = selectedDay,
            onDayClick = { selectedDay = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Посмотреть рекомендации",
            color = BlackText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(14.dp))

        Image(
            painter = painterResource(id = R.drawable.widget_personal_recommendations),
            contentDescription = "Защита кожи",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSmall))
                .clickable { onRecommendationsClick() }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun UvChartCard(
    values: List<Float>,
    hours: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(CornerMedium))
            .padding(18.dp)
    ) {
        Text(
            text = "УФ-индекс за день",
            color = BlackText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            values.forEachIndexed { index, value ->
                UvBar(
                    value = value,
                    hour = hours.getOrElse(index) { "" }
                )
            }
        }
    }
}

@Composable
fun UvBar(
    value: Float,
    hour: String
) {
    val maxUv = 10f
    val barHeight = (value / maxUv * 140).dp.coerceAtLeast(8.dp)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = String.format("%.1f", value),
            color = InputPlaceholder,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(18.dp)
                .height(barHeight)
                .background(MainBeige, RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = hour,
            color = InputPlaceholder,
            fontSize = 12.sp
        )
    }
}

@Composable
fun SmallUvCard(
    currentUv: Float,
    riskLevel: String,
    skinType: String,
    maxSunTime: String
) {
    val cardColor = when (riskLevel) {
        "низкий" -> Color(0xFF69C86F)
        "умеренный" -> Color(0xFFE6B84F)
        "высокий" -> Color(0xFFE58B45)
        "очень высокий" -> Color(0xFFD96A6A)
        "экстремальный" -> Color(0xFF9B4D9D)
        else -> Color(0xFF69C86F)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(118.dp)
            .background(cardColor, RoundedCornerShape(CornerSmall))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(110.dp)
            ) {
                Text(
                    text = String.format("%.1f", currentUv),
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 42.sp
                )

                Text(
                    text = riskLevel,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(72.dp)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(22.dp))

            Column {
                Text(
                    text = skinType,
                    color = Color.White,
                    fontSize = 15.sp
                )

                Text(
                    text = maxSunTime,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Макс. время на солнце",
                    color = Color.White,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun DaySelector(
    days: List<String>,
    selectedDay: Int,
    onDayClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, day ->
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .width(44.dp)
                    .background(
                        if (selectedDay == index) MainBeige else Color.White,
                        RoundedCornerShape(CornerSmall)
                    )
                    .clickable { onDayClick(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    color = if (selectedDay == index) Color.White else BlackText,
                    fontSize = if (day.length > 3) 10.sp else 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

fun calculateRiskLevel(uv: Float): String {
    return when {
        uv < 3f -> "низкий"
        uv < 6f -> "умеренный"
        uv < 8f -> "высокий"
        uv < 11f -> "очень высокий"
        else -> "экстремальный"
    }
}

fun calculateMaxSunTime(uv: Float, phototype: String): String {
    val baseMinutes = when (phototype) {
        "I-II" -> 180
        "III" -> 260
        "IV" -> 380
        "V" -> 540
        "VI" -> 760
        else -> 180
    }

    val safeMinutes = (baseMinutes / uv.coerceAtLeast(1f)).toInt()

    val hours = safeMinutes / 60
    val minutes = safeMinutes % 60

    return if (hours > 0) {
        "$hours ч $minutes мин"
    } else {
        "$minutes мин"
    }
}