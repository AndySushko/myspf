package com.example.myspf.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.R
import com.example.myspf.ui.theme.*

@Composable
fun UvIndexScreen(
    onBackClick: () -> Unit,
    onRecommendationsClick: () -> Unit
) {
    var selectedDay by remember { mutableStateOf(0) }

    val days = listOf("Сегодня", "Завтра", "Ср", "Чт", "Пт", "Сб", "Вс")
    val uvValues = listOf(0.4f, 0.8f, 1.6f, 2.3f, 2.1f, 1.4f, 0.7f, 0.2f)
    val hours = listOf("6", "8", "10", "12", "14", "16", "18", "20")

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

        UvChartCard(
            values = uvValues,
            hours = hours
        )

        Spacer(modifier = Modifier.height(14.dp))

        SmallUvCard()

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
                    hour = hours[index]
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
fun SmallUvCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(118.dp)
            .background(Color(0xFF69C86F), RoundedCornerShape(CornerSmall))
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
                    text = "2.3",
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 42.sp
                )

                Text(
                    text = "низкий",
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
                    text = "I-II тип",
                    color = Color.White,
                    fontSize = 15.sp
                )

                Text(
                    text = "1 ч 17 мин",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Максимальное время на солнце",
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