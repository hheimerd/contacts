package com.hheimerd.hangouts.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.styles.avatarSize

@Composable
fun AvatarDefault(contact: Contact, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .avatarSize()
            .drawBehind {
                drawCircle(color)
            }
            .then(modifier),
    ) {
        Text(
            contact.name.first().uppercaseChar().toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 6.dp)
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
        modifier = Modifier.avatarSize().then(modifier)
    )
}