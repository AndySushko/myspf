package com.example.myspf.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.network.PhototypeUpdateRequest
import com.example.myspf.network.RetrofitClient
import com.example.myspf.session.UserSession
import com.example.myspf.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AnalysisScreen(
    onContinueClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val neuralResult = remember {
        UserSession.neuralPhototype ?: "Не определён"
    }

    val questionnaireResult = remember {
        UserSession.questionnairePhototype ?: "Не определён"
    }

    val confidence = remember {
        UserSession.neuralConfidence
    }

    var isLoading by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    val isConsistent = neuralResult == questionnaireResult &&
            neuralResult != "Не определён" &&
            questionnaireResult != "Не определён"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .padding(horizontal = ScreenHorizontalPadding),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Результат анализа",
            color = BlackText,
            fontSize = 34.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(34.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(CornerMedium))
                .border(
                    width = 1.dp,
                    color = InputBorder,
                    shape = RoundedCornerShape(CornerMedium)
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = if (isConsistent) {
                        "Фототип определён"
                    } else {
                        "Результаты противоречивы"
                    },
                    color = BlackText,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Ответ нейросети: $neuralResult",
                    color = BlackText,
                    fontSize = 20.sp
                )

                if (confidence != null) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Уверенность модели: ${"%.1f".format(confidence * 100)}%",
                        color = InputPlaceholder,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Ответ анкеты: $questionnaireResult",
                    color = BlackText,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (isConsistent) {
                        "На основе фотографии и ответов анкеты система определила ваш фототип кожи как $neuralResult."
                    } else {
                        "Ответ нейросети и ответы анкеты не совпадают. Рекомендуется повторить проверку: сделать фото при хорошем освещении и пройти вопросы заново."
                    },
                    color = InputPlaceholder,
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                )

                if (errorText.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = errorText,
                        color = Color(0xFFC96F6F),
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (isConsistent) {
                    val userId = UserSession.userId

                    if (userId == null) {
                        errorText = "Ошибка: пользователь не найден"
                        return@Button
                    }

                    scope.launch {
                        try {
                            isLoading = true
                            errorText = ""

                            val response = RetrofitClient.api.updatePhototype(
                                userId = userId,
                                request = PhototypeUpdateRequest(
                                    phototype = neuralResult
                                )
                            )

                            if (response.isSuccessful) {
                                val updatedUser = response.body()

                                UserSession.phototype = updatedUser?.phototype ?: neuralResult

                                onContinueClick()
                            } else {
                                errorText = "Не удалось сохранить фототип"
                            }
                        } catch (e: Exception) {
                            errorText = "Ошибка подключения к серверу"
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    UserSession.neuralPhototype = null
                    UserSession.neuralConfidence = null
                    UserSession.questionnairePhototype = null
                    onRetryClick()
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainBeige,
                disabledContainerColor = InputBorder
            ),
            shape = RoundedCornerShape(CornerMedium),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Text(
                text = when {
                    isLoading -> "Сохранение..."
                    isConsistent -> "Продолжить"
                    else -> "Повторить проверку"
                },
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}