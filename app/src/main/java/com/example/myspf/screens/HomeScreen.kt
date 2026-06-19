package com.example.myspf.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspf.R
import com.example.myspf.ui.theme.*
import com.example.myspf.session.UserSession

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPhototypeClick: () -> Unit = {},
    onRecommendationsClick: () -> Unit = {},
    onUvClick: () -> Unit = {}
) {
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
                text = "Главная",
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
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onProfileClick() }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        UvIndexCard(
            phototype = UserSession.phototype ?: "не указан",
            onClick = onUvClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.widget_personal_recommendations),
            contentDescription = "Защита кожи",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSmall))
                .clickable { onRecommendationsClick() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.widget_phototype),
            contentDescription = "Фототип",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSmall))
                .clickable { onPhototypeClick() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        WarningBlock()

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentPage = "home",
            onHomeClick = {},
            onProfileClick = onProfileClick,
            onSettingsClick = onSettingsClick
        )

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun UvIndexCard(
    phototype: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xFF69C86F), RoundedCornerShape(CornerSmall))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_sun_uv_index),
                    contentDescription = "УФ-индекс",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "УФ-индекс  ›",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(
                        text = "2.3",
                        color = Color.White,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "низкий",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(86.dp)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column {
                    Text(
                        text = "$phototype тип",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "1 ч 17 мин",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Максимальное время на солнце",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun WarningBlock() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFE9D6), RoundedCornerShape(CornerSmall))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ⓘ",
            fontSize = 21.sp,
            color = BlackText
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = "Не является медицинским заключением",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BlackText,
                lineHeight = 17.sp
            )
            Text(
                text = "Используется только в ознакомительных целях",
                fontSize = 13.sp,
                color = BlackText,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentPage: String,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                id = if (currentPage == "home") R.drawable.icon_main_page
                else R.drawable.icon_main_page_non_active
            ),
            contentDescription = "Главная",
            modifier = Modifier
                .size(28.dp)
                .clickable { onHomeClick() }
        )

        Image(
            painter = painterResource(
                id = if (currentPage == "profile") R.drawable.icon_profile_page
                else R.drawable.icon_profile_page_non_active
            ),
            contentDescription = "Профиль",
            modifier = Modifier
                .size(28.dp)
                .clickable { onProfileClick() }
        )

        Image(
            painter = painterResource(
                id = if (currentPage == "settings") R.drawable.icon_settings_page
                else R.drawable.icon_settings_page_non_active
            ),
            contentDescription = "Настройки",
            modifier = Modifier
                .size(28.dp)
                .clickable { onSettingsClick() }
        )
    }
}