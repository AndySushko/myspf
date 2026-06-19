package com.example.myspf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.myspf.screens.HomeScreen
import com.example.myspf.screens.LoginScreen
import com.example.myspf.screens.RegistrationScreen
import com.example.myspf.screens.FirstPhototypeScreen
import com.example.myspf.screens.PhotoCaptureScreen
import com.example.myspf.screens.QuestionsScreen
import com.example.myspf.screens.AnalysisScreen
import com.example.myspf.screens.GeolocationScreen
import com.example.myspf.screens.ProfileScreen
import com.example.myspf.screens.SettingsScreen
import com.example.myspf.screens.SettingsScreen
import com.example.myspf.screens.TermsScreen
import com.example.myspf.screens.RecommendationsScreen
import com.example.myspf.screens.UvIndexScreen

import com.example.myspf.ui.theme.MainBeige
import com.example.myspf.ui.theme.MyspfTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyspfTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {
        composable("start") {
            StartScreen(
                onRegistrationClick = {
                    navController.navigate("registration")
                },
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }

        composable("registration") {
            RegistrationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCreateAccountClick = {
                    navController.navigate("first_phototype")
                }
            )
        }

        composable("login") {
            LoginScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    navController.navigate("home")
                }
            )
        }

        composable("first_phototype") {
            FirstPhototypeScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPhotoSelected = {
                    navController.navigate("photo_capture")
                }
            )
        }

        composable("photo_capture") {
            PhotoCaptureScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onContinueClick = {
                    navController.navigate("questions")
                }
            )
        }

        composable("questions") {
            QuestionsScreen(
                onFinishClick = {
                    navController.navigate("analysis")
                }
            )
        }

        composable("analysis") {
            AnalysisScreen(
                onContinueClick = {
                    navController.navigate("geolocation")
                },
                onRetryClick = {
                    navController.navigate("first_phototype") {
                        popUpTo("first_phototype") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("geolocation") {
            GeolocationScreen(
                onUseLocationClick = {
                    navController.navigate("home")
                }
            )
        }

        composable("home") {
            HomeScreen(
                onProfileClick = {
                    navController.navigate("profile")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                },
                onPhototypeClick = {
                    navController.navigate("first_phototype")
                },
                onRecommendationsClick = {
                    navController.navigate("recommendations")
                },
                onUvClick = {
                    navController.navigate("uv_index")
                }
            )
        }

        composable("recommendations") {
            RecommendationsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("uv_index") {
            UvIndexScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRecommendationsClick = {
                    navController.navigate("recommendations")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onHomeClick = {
                    navController.navigate("home")
                },
                onSettingsClick = {
                    navController.navigate("settings")
                },
                onRetakePhototypeClick = {
                    navController.navigate("first_phototype")
                },
                onLogoutClick = {
                    navController.navigate("start") {
                        popUpTo("start") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                onHomeClick = {
                    navController.navigate("home")
                },
                onProfileClick = {
                    navController.navigate("profile")
                },
                onTermsClick = {
                    navController.navigate("terms")
                }
            )
        }

        composable("terms") {
            TermsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

    }
}

@Composable
fun StartScreen(
    onRegistrationClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBeige),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "МОЙ\nСПФ",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 48.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Ваш помощник\nдля защиты кожи",
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = onRegistrationClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    text = "Регистрация",
                    color = Color.White,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                Text(
                    text = "Вход",
                    color = MainBeige,
                    fontSize = 28.sp
                )
            }
        }
    }
}