package com.example.myspf.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.R
import com.example.myspf.ui.theme.*

@Composable
fun TermsScreen(
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

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_back_arrow),
                contentDescription = "Назад",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBackClick() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Условия пользования",
                fontSize = 30.sp,
                color = BlackText,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = """
                Приложение «МОЙ СПФ» предназначено для ознакомительного анализа фототипа кожи и формирования рекомендаций по защите от ультрафиолетового излучения.

                Результаты, отображаемые в приложении, не являются медицинским заключением, диагнозом или заменой консультации врача. При наличии заболеваний кожи, выраженных реакций на солнце, подозрительных новообразований или иных медицинских вопросов необходимо обратиться к специалисту.

                Приложение может использовать фотографию лица пользователя для определения предполагаемого фототипа кожи. Точность результата зависит от качества фотографии, освещения, положения лица и корректности ответов пользователя на вопросы анкеты.

                Рекомендации по SPF, времени пребывания на солнце, использованию очков, головных уборов и дополнительной защиты формируются на основе фототипа кожи и данных об УФ-индексе.

                Пользователь самостоятельно принимает решение об использовании рекомендаций приложения. Разработчик не несёт ответственности за последствия неправильного использования солнцезащитных средств или длительного пребывания на солнце.

                Используя приложение, пользователь подтверждает, что ознакомлен с указанными условиями.
            """.trimIndent(),
            fontSize = 17.sp,
            color = BlackText,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}