package com.example.myspf.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.R
import com.example.myspf.ui.theme.*

@Composable
fun FirstPhototypeScreen(
    onBackClick: () -> Unit,
    onPhotoSelected: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ScreenHorizontalPadding),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_back_arrow),
                    contentDescription = "Назад",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() }
                )
            }

            Spacer(modifier = Modifier.height(96.dp))

            Text(
                text = "Давайте определим\nваш фототип",
                color = BlackText,
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 42.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Для этого нужно получить фотографию вашего\nлица",
                color = InputPlaceholder,
                fontSize = 18.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(34.dp))

            Image(
                painter = painterResource(id = R.drawable.widget_phototype),
                contentDescription = "Определить фототип",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CornerSmall))
                    .clickable {
                        showDialog = true
                    }
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "*Вы сможете изменить фототип позже в профиле",
                color = InputPlaceholder,
                fontSize = 16.sp
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = "Выберите способ",
                        fontWeight = FontWeight.Medium
                    )
                },
                text = {
                    Text("Как вы хотите получить фотографию лица?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            onPhotoSelected()
                        }
                    ) {
                        Text(
                            text = "Загрузить фото",
                            color = MainBeige
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            onPhotoSelected()
                        }
                    ) {
                        Text(
                            text = "Сделать фото",
                            color = MainBeige
                        )
                    }
                }
            )
        }
    }
}