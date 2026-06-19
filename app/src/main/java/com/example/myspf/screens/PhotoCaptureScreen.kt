package com.example.myspf.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myspf.R
import com.example.myspf.network.RetrofitClient
import com.example.myspf.session.UserSession
import com.example.myspf.ui.theme.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

@Composable
fun PhotoCaptureScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
        cameraBitmap = null
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        cameraBitmap = bitmap
        selectedImageUri = null
    }

    fun makeMultipart(): MultipartBody.Part? {
        val bytes: ByteArray = when {
            selectedImageUri != null -> {
                context.contentResolver
                    .openInputStream(selectedImageUri!!)
                    ?.readBytes()
                    ?: return null
            }

            cameraBitmap != null -> {
                val stream = ByteArrayOutputStream()
                cameraBitmap!!.compress(Bitmap.CompressFormat.JPEG, 95, stream)
                stream.toByteArray()
            }

            else -> return null
        }

        val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            name = "file",
            filename = "face.jpg",
            body = requestBody
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(horizontal = ScreenHorizontalPadding),
        horizontalAlignment = Alignment.Start
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
                text = "Фото лица",
                fontSize = 34.sp,
                color = BlackText,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = "Получите фотографию лица",
            fontSize = 30.sp,
            color = BlackText,
            fontWeight = FontWeight.Medium,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Загрузите фотографию из галереи или сделайте фото.",
            fontSize = 18.sp,
            color = InputPlaceholder,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(CornerSmall))
                .background(Color.White)
                .border(1.dp, InputBorder, RoundedCornerShape(CornerSmall)),
            contentAlignment = Alignment.Center
        ) {
            when {
                selectedImageUri != null -> {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Выбранное фото",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                cameraBitmap != null -> {
                    Image(
                        bitmap = cameraBitmap!!.asImageBitmap(),
                        contentDescription = "Фото с камеры",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Text(
                        text = "Фото не выбрано",
                        color = InputPlaceholder,
                        fontSize = 20.sp
                    )
                }
            }
        }

        if (errorText.isNotBlank()) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = errorText,
                color = Color(0xFFC96F6F),
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { galleryLauncher.launch("image/*") },
            colors = ButtonDefaults.buttonColors(containerColor = MainBeige),
            shape = RoundedCornerShape(CornerMedium),
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) {
            Text("Загрузить из галереи", color = Color.White, fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { cameraLauncher.launch(null) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(CornerMedium),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .border(1.dp, MainBeige, RoundedCornerShape(CornerMedium))
        ) {
            Text("Сделать фото", color = MainBeige, fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val filePart = makeMultipart()

                if (filePart == null) {
                    errorText = "Сначала выберите или сделайте фото"
                    return@Button
                }

                scope.launch {
                    try {
                        isLoading = true
                        errorText = ""

                        val response = RetrofitClient.api.predictPhototype(filePart)

                        if (response.isSuccessful) {
                            val result = response.body()

                            UserSession.neuralPhototype = result?.phototype
                            UserSession.neuralConfidence = result?.confidence

                            onContinueClick()
                        } else {
                            errorText = "Не удалось определить фототип"
                        }
                    } catch (e: Exception) {
                        errorText = "Ошибка подключения к серверу"
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading && (selectedImageUri != null || cameraBitmap != null),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainBeige,
                disabledContainerColor = InputBorder
            ),
            shape = RoundedCornerShape(CornerMedium),
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) {
            Text(
                text = if (isLoading) "Анализ..." else "Продолжить",
                color = Color.White,
                fontSize = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}