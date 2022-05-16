package com.hheimerd.hangouts.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.styles.avatarSize

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
    Image(
        rememberImagePainter(imageUri),
        description,
        modifier = Modifier
            .avatarSize()
            .then(modifier)
    )
}

@Preview
@Composable
fun PreviewAvatarDefault() {
    AvatarDefault('k', color = Color.Cyan)
}