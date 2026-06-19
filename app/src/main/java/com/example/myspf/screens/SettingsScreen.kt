package com.example.myspf.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.R
import com.example.myspf.ui.theme.*

@Composable
fun SettingsScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTermsClick: () -> Unit
) {
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
                text = "Настройки",
                fontSize = 30.sp,
                color = BlackText,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "Андрей",
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

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1), RoundedCornerShape(CornerMedium))
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            SettingsRow(
                title = "Условия пользования",
                onClick = onTermsClick
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentPage = "settings",
            onHomeClick = onHomeClick,
            onProfileClick = onProfileClick,
            onSettingsClick = {}
        )

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun SettingsRow(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = BlackText,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "›",
            color = BlackText,
            fontSize = 26.sp
        )
    }
}