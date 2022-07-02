package com.hheimerd.hangouts.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.ui.styles.avatarSmallSize
import com.hheimerd.hangouts.utils.InternalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AvatarDefault(letter: Char, color: Color, modifier: Modifier = Modifier, fontSize: TextUnit = 4.em) {
    Box(contentAlignment= Alignment.Center,
        modifier = Modifier
            .background(color, shape = CircleShape)
            .then(modifier)
            .layout(){ measurable, constraints ->
                // Measure the composable
                val placeable = measurable.measure(constraints)

                //get the current max dimension to assign width=height
                val currentHeight = placeable.height
                var heightCircle = currentHeight
                if (placeable.width > heightCircle)
                    heightCircle = placeable.width

                //assign the dimension and the center position
                layout(heightCircle, heightCircle) {
                    // Where the composable gets placed
                    placeable.placeRelative(0, (heightCircle-currentHeight)/2)
                }
            }) {

        Text(
            text = letter.uppercaseChar().toString(),
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = fontSize,
            modifier = Modifier.padding(4.dp).defaultMinSize(24.dp) //Use a min size for short text.
        )
    }
}

@Composable
fun Avatar(imageUri: String, modifier: Modifier = Modifier) {
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
            .avatarSmallSize()
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