package com.example.myspf.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.ui.theme.*

@Composable
fun GeolocationScreen(
    onUseLocationClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(horizontal = ScreenHorizontalPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Активировать\nгеолокацию",
            color = BlackText,
            fontSize = 36.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 42.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Это нужно для правильного определения УФ-индекса",
            color = InputPlaceholder,
            fontSize = 20.sp,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(34.dp))

        Button(
            onClick = onUseLocationClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainBeige
            ),
            shape = RoundedCornerShape(CornerMedium),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Text(
                text = "Использовать местоположение",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}