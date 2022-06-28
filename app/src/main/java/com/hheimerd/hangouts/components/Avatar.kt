package com.hheimerd.hangouts.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.ui.styles.avatarSize
import com.hheimerd.hangouts.utils.InternalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.logging.Logger

@Composable
fun AvatarDefault(letter: Char, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .avatarSize()
            .drawBehind {
                drawCircle(color)
            }
            .then(modifier),
    ) {
        Text(
            letter.uppercaseChar().toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
            ),
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxSize(),
            textAlign = TextAlign.Center,

            )
    }
}

@Composable
fun Avatar(imageUri: String, description: String = "", modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val fallback = ImageBitmap.imageResource(R.drawable.logo_42_png)
    val bitmapImage:MutableState<ImageBitmap> = remember(imageUri) { mutableStateOf(fallback) }

    LaunchedEffect(imageUri) {
        if (imageUri.isEmpty())
            return@LaunchedEffect
        scope.launch(Dispatchers.IO) {
            val bitmap = InternalStorage.getPhoto(context, imageUri)
            if (bitmap != null) {
                bitmapImage.value = bitmap.asImageBitmap()
            }
        }
    }
    Image(
        bitmap = bitmapImage.value,
        "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .avatarSize()
            .then(modifier)
            .clip(CircleShape)
    )
}

@Preview
@Composable
fun PreviewAvatarDefault() {
    AvatarDefault('k', color = Color.Cyan)
}

@Preview
@Composable
fun PreviewAvatar() {
    Avatar("")
}