package com.example.myspf.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun RecommendationsScreen(
    onBackClick: () -> Unit
) {
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
                text = "Защита кожи",
                fontSize = 30.sp,
                color = BlackText,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        RecommendationCard(
            title = "SPF-защита",
            text = "Сейчас рекомендуется использовать SPF 50. Наносите средство за 15–20 минут до выхода на солнце."
        )

        Spacer(modifier = Modifier.height(14.dp))

        RecommendationCard(
            title = "Повторное нанесение",
            text = "Обновляйте солнцезащитное средство каждые 2 часа, а также после купания или активного потоотделения."
        )

        Spacer(modifier = Modifier.height(14.dp))

        RecommendationCard(
            title = "Очки",
            text = "При текущем УФ-индексе рекомендуется использовать солнцезащитные очки категории CAT.3."
        )

        Spacer(modifier = Modifier.height(14.dp))

        RecommendationCard(
            title = "Вода",
            text = "Пейте воду регулярно, особенно при длительном нахождении на улице в дневное время."
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun RecommendationCard(
    title: String,
    text: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(CornerMedium))
            .padding(18.dp)
    ) {
        Text(
            text = title,
            color = BlackText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            color = InputPlaceholder,
            fontSize = 16.sp,
            lineHeight = 22.sp
        )
    }
}