package com.example.myspf.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.session.UserSession
import com.example.myspf.ui.theme.*

data class Question(
    val title: String,
    val answers: List<String>
)

@Composable
fun QuestionsScreen(
    onFinishClick: () -> Unit
) {
    val questions = listOf(
        Question(
            title = "Какого оттенка ваша кожа без загара?",
            answers = listOf("Очень светлая", "Светлая", "Средняя", "Смуглая", "Темная")
        ),
        Question(
            title = "Как кожа обычно реагирует на солнце?",
            answers = listOf("Быстро обгорает", "Иногда обгорает", "Редко обгорает", "Почти не обгорает")
        ),
        Question(
            title = "Есть ли у вас чувствительность к солнцу?",
            answers = listOf("Да, чувствительная кожа", "Нет, нормальная кожа")
        ),
        Question(
            title = "Как быстро появляется загар?",
            answers = listOf("Почти не появляется", "Медленно", "Средне", "Быстро")
        ),
        Question(
            title = "Какой у вас натуральный цвет волос?",
            answers = listOf("Светлый или рыжий", "Русый", "Каштановый", "Темный", "Черный")
        ),
        Question(
            title = "Какой у вас цвет глаз?",
            answers = listOf("Голубой или серый", "Зеленый", "Карий", "Темно-карий")
        )
    )

    var currentStep by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }

    val selectedAnswers = remember {
        mutableStateListOf<Int?>().apply {
            repeat(questions.size) { add(null) }
        }
    }

    val currentQuestion = questions[currentStep]
    val progress = currentStep + 1

    fun calculateQuestionnairePhototype(): String {
        var score = 0

        selectedAnswers.forEachIndexed { questionIndex, answerIndex ->
            if (answerIndex == null) return@forEachIndexed

            score += when (questionIndex) {
                0 -> when (answerIndex) {
                    0 -> 0
                    1 -> 1
                    2 -> 2
                    3 -> 3
                    else -> 4
                }

                1 -> when (answerIndex) {
                    0 -> 0
                    1 -> 1
                    2 -> 2
                    else -> 3
                }

                2 -> when (answerIndex) {
                    0 -> 0
                    else -> 2
                }

                3 -> when (answerIndex) {
                    0 -> 0
                    1 -> 1
                    2 -> 2
                    else -> 3
                }

                4 -> when (answerIndex) {
                    0 -> 0
                    1 -> 1
                    2 -> 2
                    3 -> 3
                    else -> 4
                }

                5 -> when (answerIndex) {
                    0 -> 0
                    1 -> 1
                    2 -> 2
                    else -> 3
                }

                else -> 0
            }
        }

        return when {
            score <= 5 -> "I-II"
            score <= 9 -> "III"
            score <= 13 -> "IV"
            score <= 17 -> "V"
            else -> "VI"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFD9B8))
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(42.dp))

        Text(
            text = "Шаг $progress / ${questions.size}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = BlackText
        )

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(Color(0xFFE7B982), RoundedCornerShape(20.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.toFloat() / questions.size.toFloat())
                    .height(12.dp)
                    .background(Color(0xFF824500), RoundedCornerShape(20.dp))
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp)
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Text(
                text = currentQuestion.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 26.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText
            )

            Spacer(modifier = Modifier.height(28.dp))

            currentQuestion.answers.forEachIndexed { index, answer ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(76.dp)
                        .background(Color(0xFFFFE0B8), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFFFD2A0), RoundedCornerShape(16.dp))
                        .clickable {
                            selectedAnswer = index
                            selectedAnswers[currentStep] = index
                        }
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedAnswer == index,
                        onClick = {
                            selectedAnswer = index
                            selectedAnswers[currentStep] = index
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF824500),
                            unselectedColor = InputPlaceholder
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = answer,
                        fontSize = 17.sp,
                        color = BlackText
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (currentStep < questions.lastIndex) {
                    currentStep++
                    selectedAnswer = selectedAnswers[currentStep]
                } else {
                    val questionnaireResult = calculateQuestionnairePhototype()
                    UserSession.questionnairePhototype = questionnaireResult
                    onFinishClick()
                }
            },
            enabled = selectedAnswer != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF824500),
                disabledContainerColor = Color(0xFFC8A77D)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
        ) {
            Text(
                text = if (currentStep < questions.lastIndex) "Продолжить" else "Завершить",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}