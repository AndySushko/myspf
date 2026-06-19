package com.example.myspf.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.R
import com.example.myspf.ui.theme.*
import com.example.myspf.session.UserSession

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRetakePhototypeClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

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
                text = "Ваш профиль",
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
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        ProfileInfoBlock()

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = onRetakePhototypeClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainBeige
            ),
            shape = RoundedCornerShape(CornerMedium),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Text(
                text = "Определить фототип заново",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = { showLogoutDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF1F1F1)
            ),
            shape = RoundedCornerShape(CornerMedium),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Text(
                text = "Выйти из профиля",
                color = Color(0xFFC96F6F),
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentPage = "profile",
            onHomeClick = onHomeClick,
            onProfileClick = {},
            onSettingsClick = onSettingsClick
        )

        Spacer(modifier = Modifier.height(18.dp))
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text("Выход из профиля")
            },
            text = {
                Text("Вы уверены, что хотите выйти из профиля?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    }
                ) {
                    Text("Да", color = MainBeige)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Нет", color = MainBeige)
                }
            }
        )
    }
}

@Composable
fun ProfileInfoBlock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F1F1), RoundedCornerShape(CornerMedium))
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        ProfileRow("Логин (почта)", UserSession.email ?: "Не указан")
        ProfileDivider()
        ProfileRow("Имя", "${UserSession.firstName ?: "Не указано"} ›")
        ProfileDivider()
        ProfileRow("Пол", "Не указан ›")
        ProfileDivider()
        ProfileRow("Фототип кожи", "${UserSession.phototype ?: "Не указан"} ›")
        ProfileDivider()
        ProfileRow("Регион", "Краснодар, Россия ›")
        ProfileDivider()
        ProfileRow("Дата рождения", "29.02.2004 ›")
    }
}

@Composable
fun ProfileRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = BlackText,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            color = BlackText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProfileDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFD9D9D9))
    )
}