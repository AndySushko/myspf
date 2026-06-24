package com.example.myspf.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
fun HomeScreen(
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPhototypeClick: () -> Unit = {},
    onRecommendationsClick: () -> Unit = {},
    onUvClick: () -> Unit = {}
) {
    val context = LocalContext.current

    var uvData by remember { mutableStateOf<UvResponse?>(null) }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            return@LaunchedEffect
        }

        try {
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
            }
        } catch (_: Exception) {
            uvData = null
        }
    }

    val phototype = UserSession.phototype ?: "не указан"
    val currentUv = uvData?.current_uv ?: 0f
    val riskLevel = uvData?.risk_level ?: "геолокация"
    val maxSunTime = uvData?.max_sun_time ?: "не активна"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(horizontal = ScreenHorizontalPadding)
    ) {
        Spacer(modifier = Modifier.height(52.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Главная",
                fontSize = 30.sp,
                color = BlackText,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = UserSession.firstName ?: "Профиль",
                fontSize = 18.sp,
                color = BlackText,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(id = R.drawable.icon_profile_black),
                contentDescription = "Профиль",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onProfileClick() }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        UvIndexCard(
            uvValue = currentUv,
            riskLevel = riskLevel,
            phototype = phototype,
            maxSunTime = maxSunTime,
            onClick = onUvClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.widget_personal_recommendations),
            contentDescription = "Защита кожи",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSmall))
                .clickable { onRecommendationsClick() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.widget_phototype),
            contentDescription = "Фототип",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSmall))
                .clickable { onPhototypeClick() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        WarningBlock()

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentPage = "home",
            onHomeClick = {},
            onProfileClick = onProfileClick,
            onSettingsClick = onSettingsClick
        )

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun UvIndexCard(
    uvValue: Float,
    riskLevel: String,
    phototype: String,
    maxSunTime: String,
    onClick: () -> Unit = {}
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
            .height(180.dp)
            .background(cardColor, RoundedCornerShape(CornerSmall))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_sun_uv_index),
                    contentDescription = "УФ-индекс",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "УФ-индекс  ›",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(
                        text = String.format("%.1f", uvValue),
                        color = Color.White,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = riskLevel,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(86.dp)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column {
                    Text(
                        text = "$phototype тип",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = maxSunTime,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Макс. время на солнце",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun WarningBlock() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFE9D6), RoundedCornerShape(CornerSmall))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ⓘ",
            fontSize = 21.sp,
            color = BlackText
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = "Не является медицинским заключением",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                lineHeight = 17.sp
            )
            Text(
                text = "Используется только в ознакомительных целях",
                fontSize = 13.sp,
                color = BlackText,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentPage: String,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                id = if (currentPage == "home") R.drawable.icon_main_page
                else R.drawable.icon_main_page_non_active
            ),
            contentDescription = "Главная",
            modifier = Modifier
                .size(28.dp)
                .clickable { onHomeClick() }
        )

        Image(
            painter = painterResource(
                id = if (currentPage == "profile") R.drawable.icon_profile_page
                else R.drawable.icon_profile_page_non_active
            ),
            contentDescription = "Профиль",
            modifier = Modifier
                .size(28.dp)
                .clickable { onProfileClick() }
        )

        Image(
            painter = painterResource(
                id = if (currentPage == "settings") R.drawable.icon_settings_page
                else R.drawable.icon_settings_page_non_active
            ),
            contentDescription = "Настройки",
            modifier = Modifier
                .size(28.dp)
                .clickable { onSettingsClick() }
        )
    }
}