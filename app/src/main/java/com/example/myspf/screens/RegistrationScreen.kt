package com.example.myspf.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.R
import com.example.myspf.network.RegisterRequest
import com.example.myspf.network.RetrofitClient
import com.example.myspf.ui.theme.*
import kotlinx.coroutines.launch

import com.example.myspf.session.UserSession

@Composable
fun RegistrationScreen(
    onBackClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    var errorText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

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
                text = "Регистрация",
                fontSize = 34.sp,
                color = BlackText,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = "Логин (почта) и пароль",
            fontSize = 24.sp,
            color = BlackText,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(18.dp))

        AppTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Эл. почта"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AppTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Пароль"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AppTextField(
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            placeholder = "Повторите пароль"
        )

        Spacer(modifier = Modifier.height(34.dp))

        Text(
            text = "Данные аккаунта",
            fontSize = 24.sp,
            color = BlackText,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(18.dp))

        AppTextField(
            value = firstName,
            onValueChange = { firstName = it },
            placeholder = "Имя*"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AppTextField(
            value = lastName,
            onValueChange = { lastName = it },
            placeholder = "Фамилия"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AppTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            placeholder = "Дата рождения* (ГГГГ-ММ-ДД)"
        )

        if (errorText.isNotBlank()) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = errorText,
                color = Color(0xFFC96F6F),
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = {
                errorText = ""

                if (email.isBlank() || password.isBlank() || firstName.isBlank() || birthDate.isBlank()) {
                    errorText = "Заполните обязательные поля"
                    return@Button
                }

                if (password != repeatPassword) {
                    errorText = "Пароли не совпадают"
                    return@Button
                }

                scope.launch {
                    try {
                        isLoading = true

                        val response = RetrofitClient.api.register(
                            RegisterRequest(
                                email = email.trim(),
                                password = password,
                                first_name = firstName.trim(),
                                last_name = lastName.trim().ifBlank { null },
                                birth_date = birthDate.trim()
                            )
                        )

                        if (response.isSuccessful) {
                            val user = response.body()

                            UserSession.userId = user?.id
                            UserSession.email = user?.email
                            UserSession.firstName = user?.first_name
                            UserSession.phototype = user?.phototype

                            onCreateAccountClick()
                        } else {
                            errorText = "Пользователь уже существует или данные заполнены неверно"
                        }
                    } catch (e: Exception) {
                        errorText = "Ошибка подключения к серверу"
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainBeige,
                disabledContainerColor = InputBorder
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            Text(
                text = if (isLoading) "Создание..." else "Создать аккаунт",
                color = Color.White,
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = InputPlaceholder,
                fontSize = 22.sp
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BackgroundGray,
            unfocusedContainerColor = BackgroundGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            cursorColor = Color.Black,
            focusedPlaceholderColor = InputPlaceholder,
            unfocusedPlaceholderColor = InputPlaceholder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .border(
                1.dp,
                InputBorder,
                RoundedCornerShape(20.dp)
            )
    )
}