package com.example.myspf.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.myspf.ui.theme.*

@Composable
fun GeolocationScreen(
    onUseLocationClick: () -> Unit
) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onUseLocationClick()
        } else {
            message = "Геолокация не включена. Вы сможете разрешить доступ позже на странице УФ-индекса."
        }
    }

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
            onClick = {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    onUseLocationClick()
                } else {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            },
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Продолжить без геолокации",
            color = BlackText,
            fontSize = 17.sp,
            modifier = Modifier
                .clickable { onUseLocationClick() }
                .padding(vertical = 10.dp)
        )

        if (message.isNotBlank()) {
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = message,
                color = InputPlaceholder,
                fontSize = 15.sp,
                lineHeight = 20.sp
            )
        }
    }
}